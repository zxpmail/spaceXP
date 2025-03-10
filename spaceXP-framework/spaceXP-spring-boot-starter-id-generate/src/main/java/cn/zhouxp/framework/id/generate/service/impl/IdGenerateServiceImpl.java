package cn.zhouxp.framework.id.generate.service.impl;

import cn.zhouxp.framework.id.generate.dao.mapper.IdGenerateMapper;
import cn.zhouxp.framework.id.generate.dao.po.IdGeneratePO;
import cn.zhouxp.framework.id.generate.model.LocalIdBO;
import cn.zhouxp.framework.id.generate.properties.IdGenerateProperties;
import cn.zhouxp.framework.id.generate.service.IdGenerateService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

import static cn.zhouxp.framework.id.generate.constants.Constant.ORDERED;
import static cn.zhouxp.framework.id.generate.constants.Constant.UPDATE_RATE;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-05 20:28:03
 *
 * @author zhouxp
 */
@Slf4j
public class IdGenerateServiceImpl implements IdGenerateService, InitializingBean {
    @Setter(onMethod_ = @Autowired)
    private IdGenerateMapper idGenerateMapper;

    private final static ConcurrentHashMap<String, LocalIdBO> LOCAL_ID_MAP = new ConcurrentHashMap<>();

    private final static Map<String, Semaphore> SEMAPHORE_MAP = new ConcurrentHashMap<>();

    private final static ThreadPoolTaskExecutor EXECUTOR = new ThreadPoolTaskExecutor();

    private final Integer retryTimes;

    public IdGenerateServiceImpl(IdGenerateProperties idGenerateProperties) {
        EXECUTOR.setMaxPoolSize(idGenerateProperties.getThread().getMaxPoolSize());
        //核心线程数
        EXECUTOR.setCorePoolSize(idGenerateProperties.getThread().getCorePoolSize());
        //任务队列的大小
        EXECUTOR.setQueueCapacity(idGenerateProperties.getThread().getQueueCapacity());
        //线程前缀名
        EXECUTOR.setThreadNamePrefix(idGenerateProperties.getThread().getNamePrefix());
        //线程存活时间
        EXECUTOR.setKeepAliveSeconds(idGenerateProperties.getThread().getKeepAliveSeconds());

        EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //线程初始化
        EXECUTOR.initialize();
        retryTimes = idGenerateProperties.getRetryTimes();
    }

    @Override
    public Long generateId(String biz) {
        if (!StringUtils.hasText(biz)) {
            log.error("[generateId] id is error,biz is null");
            return null;
        }
        LocalIdBO localIdBO = LOCAL_ID_MAP.get(biz);
        if (localIdBO == null) {
            log.error("[generateId] localIdBO is null,id is {}", biz);
            return null;
        }
        this.refreshLocalId(localIdBO);
        Long returnId;
        if (localIdBO.getOrdered() == ORDERED) {
            returnId = localIdBO.getCurrentOrderId().incrementAndGet();
        } else {
            if (localIdBO.getUnorderedIdQueue() == null) {
                log.error("[generateId] unorderedIdQueue is null,id is {}", biz);
                return null;
            }
            returnId = localIdBO.getUnorderedIdQueue().poll();
        }
        if (returnId == null || returnId > localIdBO.getEnd()) {
            //同步去刷新
            log.error("[generateId] id is over limit,id is {}", biz);
            return null;
        }
        return returnId;
    }

    private void refreshLocalId(LocalIdBO localIdBO) {
        boolean isRefresh = false;
        synchronized (localIdBO) {
            if (localIdBO.getOrdered() == ORDERED) {
                long step = localIdBO.getEnd() - localIdBO.getStart();
                if (localIdBO.getCurrentOrderId().get() - localIdBO.getStart() > step * UPDATE_RATE) {
                    isRefresh = true;
                }
            } else {
                long begin = localIdBO.getStart();
                long end = localIdBO.getEnd();
                long remainSize = localIdBO.getUnorderedIdQueue().size();
                if ((end - begin) * (1 - UPDATE_RATE) > remainSize) {
                    isRefresh = true;
                }
            }
        }
        if (isRefresh) {
            Semaphore semaphore = SEMAPHORE_MAP.get(localIdBO.getBiz());
            if (semaphore == null) {
                log.error("semaphore is null,id is {}", localIdBO.getBiz());
                return;
            }
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                log.info("开始尝试进行本地id段的同步操作");
                //异步进行同步id段操作
                EXECUTOR.execute(() -> {
                    try {
                        IdGeneratePO idGenerate = idGenerateMapper.selectById(localIdBO.getId());
                        tryUpdateRecord(idGenerate);
                    } catch (Exception e) {
                        log.error("[refreshLocalSeqId] error is ", e);
                    } finally {
                        SEMAPHORE_MAP.get(localIdBO.getBiz()).release();
                        log.info("本地有序id段同步完成,id is {}", localIdBO.getBiz());
                    }
                });
            }
        }
    }

    private void tryUpdateRecord(IdGeneratePO idGenerate) {
        int updateResult = idGenerateMapper.updateNewIdVersion(idGenerate.getId(), idGenerate.getVersion());
        if (updateResult > 0) {
            localIdHandler(idGenerate);
            return;
        }
        //重试进行更新
        for (int i = 0; i < retryTimes; i++) {
            idGenerate = idGenerateMapper.selectById(idGenerate.getId());
            updateResult = idGenerateMapper.updateNewIdVersion(idGenerate.getId(), idGenerate.getVersion());
            if (updateResult > 0) {
                localIdHandler(idGenerate);
                return;
            }
        }
        throw new RuntimeException("表id段占用失败，竞争过于激烈，id is " + idGenerate.getId());
    }

    private void localIdHandler(IdGeneratePO idGenerate) {
        long start = idGenerate.getStart();
        long end = idGenerate.getEnd();
        LocalIdBO localIdBO = new LocalIdBO();
        localIdBO.setId(idGenerate.getId());
        localIdBO.setStart(start);
        localIdBO.setEnd(end);
        localIdBO.setBiz(idGenerate.getBiz());
        if (idGenerate.getOrdered() == ORDERED) {
            val atomicLong = new AtomicLong(start);
            localIdBO.setCurrentOrderId(atomicLong);
        } else {

            List<Long> idList = new ArrayList<>();
            for (long i = start; i < end; i++) {
                idList.add(i);
            }
            //将本地id段提前打乱，然后放入到队列中
            Collections.shuffle(idList);
            ConcurrentLinkedQueue<Long> idQueue = new ConcurrentLinkedQueue<>(idList);
            localIdBO.setUnorderedIdQueue(idQueue);
        }
        LOCAL_ID_MAP.put(localIdBO.getBiz(), localIdBO);
    }

    @Override
    public void afterPropertiesSet()  {
        List<IdGeneratePO> idGenerateList = idGenerateMapper.selectAll();
        for (IdGeneratePO idGenerate : idGenerateList) {
            log.info("服务刚启动，抢占新的id段");
            tryUpdateRecord(idGenerate);
            SEMAPHORE_MAP.put(idGenerate.getBiz(), new Semaphore(1));
        }
    }
}

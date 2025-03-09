package cn.zhouxp.framework.id.generate.service.impl;

import cn.zhouxp.framework.id.generate.dao.mapper.IdGenerateMapper;
import cn.zhouxp.framework.id.generate.model.LocalIdBO;
import cn.zhouxp.framework.id.generate.service.IdGenerateService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-05 20:28:03
 *
 * @author zhouxp
 */
@Service
public class IdGenerateServiceImpl implements IdGenerateService {
    @Setter(onMethod_ = @Autowired)
    private IdGenerateMapper idGenerateMapper;

    private final static Map<String, LocalIdBO> LOCAL_ID_MAP = new ConcurrentHashMap<>();

    private final static Map<String, Semaphore> SEMAPHORE_MAP = new ConcurrentHashMap<>();


    @Override
    public Long generateId(String biz) {
        return 0L;
    }
}

package cn.piesat.framework.web.core;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :统计每个Bean的初始化耗时
 * <p/>
 * <b>@create:</b> 2023/2/26 9:31.
 *
 * @author zhouxp
 */
@Slf4j
public class TimeCostBeanPostProcessor implements BeanPostProcessor {
    private final Map<String, Long> costMap = Maps.newConcurrentMap();

    /**
     * 初始化bean时记录时间
     */
    @Override
    public Object postProcessBeforeInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        costMap.put(beanName, System.currentTimeMillis());
        return bean;
    }

    /**
     *结束初始化bean时显示时间
     */
    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        if (costMap.containsKey(beanName)) {
            Long start = costMap.get(beanName);
            long cost = System.currentTimeMillis() - start;
            if (cost > 0) {
                costMap.put(beanName, cost);
                log.info("bean:{} \ttime:{}",beanName,cost);
            }
        }
        return bean;
    }
}

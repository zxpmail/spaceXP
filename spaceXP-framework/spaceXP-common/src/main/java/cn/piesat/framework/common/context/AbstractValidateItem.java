package cn.piesat.framework.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p/>
 * {@code @description}: 获取 Spring 应用上下文的抽象类
 * <p/>
 * {@code @create}: 2024-10-18 17:22
 * {@code @author}: zhouxp
 */
public  abstract class AbstractValidateItem implements ApplicationContextAware {
    private ConfigurableApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.context = (ConfigurableApplicationContext) applicationContext;
        }

        validate();
    }

    public abstract void validate();

    public ConfigurableApplicationContext getContext() {
        return context;
    }
}

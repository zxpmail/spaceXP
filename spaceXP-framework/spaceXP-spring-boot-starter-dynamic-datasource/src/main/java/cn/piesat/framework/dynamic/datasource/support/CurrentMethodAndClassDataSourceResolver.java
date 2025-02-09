package cn.piesat.framework.dynamic.datasource.support;

import cn.piesat.framework.dynamic.datasource.annotation.DS;
import org.springframework.core.MethodClassKey;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * <p/>
 * {@code @description}: 当前类及方法上面查找注解
 * <p/>
 * {@code @create}: 2025-02-09 18:32
 * {@code @author}: zhouxp
 */
public class CurrentMethodAndClassDataSourceResolver extends AbstractDataSourceClassResolver{
    @Override
    public String findKey(Method method, Object targetObject) {
        //获取目标类
        Class<?> targetObjectClass = targetObject.getClass();

        MethodClassKey methodClassKey = new MethodClassKey(method, targetObjectClass);
        String dsName = getCache(methodClassKey);
        if (StringUtils.hasText(dsName)) {
            return dsName;
        }
        dsName = findDataSourceAttribute(method, DS.class);
        if (StringUtils.hasText(dsName)) {
            putCache(methodClassKey, dsName);
            return dsName;
        }

        //如果当前方法上面没有注解，就获取用户自定义的类
        Class<?> userClass = ClassUtils.getUserClass(targetObjectClass);
        dsName = findDataSourceAttribute(userClass, DS.class);
        if (StringUtils.hasText(dsName) && ClassUtils.isUserLevelMethod(method)) {
            putCache(methodClassKey, dsName);
            return dsName;
        }
        return null;
    }
}

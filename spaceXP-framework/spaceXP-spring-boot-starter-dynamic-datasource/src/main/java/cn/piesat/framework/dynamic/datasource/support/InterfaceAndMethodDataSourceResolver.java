package cn.piesat.framework.dynamic.datasource.support;

import cn.piesat.framework.dynamic.datasource.annotation.DS;
import org.springframework.core.MethodClassKey;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * <p/>
 * {@code @description}: 接口以及接口的方法上面查找@DS
 * <p/>
 * {@code @create}: 2025-02-09 18:59
 * {@code @author}: zhouxp
 */
public class InterfaceAndMethodDataSourceResolver extends AbstractDataSourceClassResolver{
    @Override
    public String findKey(Method method, Object targetObject) {

        Class<?> targetObjectClass = targetObject.getClass();

        MethodClassKey methodClassKey = new MethodClassKey(method, targetObjectClass);
        String dsName = getCache(methodClassKey);
        if (StringUtils.hasText(dsName)) {
            return dsName;
        }
        Class<?> userClass = ClassUtils.getUserClass(targetObjectClass);

        for (Class<?> interfaceClazz: ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            dsName = findDataSourceAttribute(interfaceClazz, DS.class);
            if (StringUtils.hasText(dsName)) {
                putCache(methodClassKey, dsName);
                return dsName;
            }
            //如果接口上面找不到，就去接口上面的方法查找
            Method specificMethod = ReflectionUtils.findMethod(interfaceClazz, method.getName(), method.getParameterTypes());
            dsName = findDataSourceAttribute(specificMethod, DS.class);
            if (StringUtils.hasText(dsName)) {
                putCache(methodClassKey, dsName);
                return dsName;
            }
        }
        return null;
    }
}

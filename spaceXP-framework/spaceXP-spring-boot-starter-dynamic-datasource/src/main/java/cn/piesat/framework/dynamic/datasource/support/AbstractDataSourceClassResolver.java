package cn.piesat.framework.dynamic.datasource.support;

import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}: 抽象数据源解析类
 * <p/>
 * {@code @create}: 2025-02-09 18:26
 * {@code @author}: zhouxp
 */
public abstract class AbstractDataSourceClassResolver implements DataSourceClassResolver{

    private final Map<Object, String> DS_CACHE = new ConcurrentHashMap<>();
    /**
     * 查找对应节点上的注解信息
     */
    protected String findDataSourceAttribute(AnnotatedElement element, Class<? extends Annotation> annotation) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(element, annotation);
        if (attributes != null) {
            return attributes.getString("value");
        }
        return null;
    }

    protected String getCache(Object cacheKey) {
        return DS_CACHE.get(cacheKey);
    }

    protected void putCache(Object cacheKey, String dsName) {
        this.DS_CACHE.put(cacheKey, dsName);
    }
}

package cn.piesat.framework.dynamic.datasource.support;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * <p/>
 * {@code @description}: 抽象数据源解析类
 * <p/>
 * {@code @create}: 2025-02-09 18:26
 * {@code @author}: zhouxp
 */
public abstract class AbstractDataSourceClassResolver implements DataSourceClassResolver{
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
}

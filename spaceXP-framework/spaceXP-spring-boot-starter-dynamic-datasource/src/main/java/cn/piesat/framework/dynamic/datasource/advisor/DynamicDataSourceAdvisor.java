package cn.piesat.framework.dynamic.datasource.advisor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p/>
 * {@code @description}: 动态数据源通知Advisor = 动态多数据源增强业务逻辑Advice +  动态多数据源切点 pointcut
 * <p/>
 * {@code @create}: 2025-02-09 18:01
 * {@code @author}: zhouxp
 */
public class DynamicDataSourceAdvisor extends AbstractPointcutAdvisor {
    private final Advice advice;
    private final Pointcut pointcut;
    public DynamicDataSourceAdvisor(Advice advice, Class<? extends Annotation> annotation) {
        this.advice = advice;
        this.pointcut = buildPointCut(annotation);
    }

    private Pointcut buildPointCut(Class<? extends Annotation> annotation) {
        // 匹配类级别的切点，包括父类和接口
        Pointcut cpc = new AnnotationMatchingPointcut(annotation, true);
        // 匹配方法级别的切点
        Pointcut mpc = new AnnotationMethodPointCut(annotation);
        return new ComposablePointcut(cpc).union(mpc);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    /**
     * 自定义方法级别的切点
     */
    private static class AnnotationMethodPointCut implements Pointcut {
        Class<? extends Annotation> annotation;
        public AnnotationMethodPointCut(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new AnnotationMethodMatcher(annotation);
        }

        private static class AnnotationMethodMatcher extends StaticMethodMatcher {
            Class<? extends Annotation> annotation;
            public AnnotationMethodMatcher(Class<? extends Annotation> annotation) {
                this.annotation = annotation;
            }

            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                if (AnnotatedElementUtils.hasAnnotation(method, this.annotation)) {
                    return true;
                }
                //动态代理类，不会有原类的注解，直接会放弃，也就是匹配不到
                if (Proxy.isProxyClass(targetClass)) {
                    return false;
                }
                //如果不是代理类，这个时候可能是接口或者超类，那么拿到具体的子类去解析
                Method mostSpecificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
                return mostSpecificMethod != method && AnnotatedElementUtils.hasAnnotation(method, this.annotation);
            }
        }
    }
}

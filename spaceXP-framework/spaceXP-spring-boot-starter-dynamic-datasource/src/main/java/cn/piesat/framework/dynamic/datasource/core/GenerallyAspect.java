package cn.piesat.framework.dynamic.datasource.core;

import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceContextHolder;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :一般的Aspect 拦截上层及本层
 * 在启动类中增加@ImportResource("classpath:applicationContext.xml")
 * applicationContext.xml文件如下
 * <beans xmlns="http://www.springframework.org/schema/beans"
 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * xmlns:aop="http://www.springframework.org/schema/aop"
 * xsi:schemaLocation="http://www.springframework.org/schema/beans
 * http://www.springframework.org/schema/beans/spring-beans.xsd
 * http://www.springframework.org/schema/aop
 * http://www.springframework.org/schema/aop/spring-aop.xsd">
 * <p>
 * <!-- 配置切面 -->
 * <bean id="myAspect" class="cn.piesat.framework.dynamic.datasource.core.GenerallyAspect" />
 * <p>
 * <!-- 配置 AOP -->
 * <aop:config>
 * <!-- 配置切点表达式 -->
 * <aop:pointcut id="myPointcut" expression="execution(* cn.piesat.dynamic.datasource.service.*.*(..))" />
 * <p>
 * <!-- 配置切面 -->
 * <aop:aspect ref="myAspect">
 * <!-- 配置 Around advice -->
 * <aop:around method="aroundAdvice" pointcut-ref="myPointcut" />
 * </aop:aspect>
 * </aop:config>
 *
 * </beans>
 * <p/>
 * <b>@create:</b> 2023/12/28 13:52.
 *
 * @author zhouxp
 */
public class GenerallyAspect {
    public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
        // 如果DataSourceContextHolder.getDataSource()有值表示上层传来的数据源放行
        if (StringUtils.hasText(DataSourceContextHolder.getDataSource())) {
            return point.proceed();
        }

        // 如果类上有DS注解 而方法上没有DS进行ds处理，方法上又DS就放行
        DS ds = getClassAnnotation(point, DS.class);
        if (!Objects.isNull(ds)) {
            Method targetMethod = getTargetMethod(point);
            if (!Objects.isNull(targetMethod) && !Objects.isNull(targetMethod.getAnnotation(DS.class))) {
                return point.proceed();
            }
            String dsName = DataSourceUtils.getDsName(point);
            String dsValue = ds.value();
            if (StringUtils.hasText(dsName)) {
                DataSourceContextHolder.setDataSource(dsName);
            } else if (StringUtils.hasText(dsValue)) {
                DataSourceContextHolder.setDataSource(dsValue);
            }
        } else {
            //默认数据源
            DataSourceContextHolder.setDataSource("__master");
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.removeDataSource();
        }
    }

    private Method getTargetMethod(ProceedingJoinPoint joinPoint) {
        // 获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();

        // 获取目标方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 获取目标方法
        try {
            return targetClass.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T extends Annotation> T getClassAnnotation(JoinPoint joinPoint, Class<T> annotationType) {
        Class<?> targetClass = getTargetClass(joinPoint);
        return targetClass != null ? targetClass.getAnnotation(annotationType) : null;
    }

    private Class<?> getTargetClass(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target != null && target.getClass().getName().contains("$$EnhancerBySpringCGLIB$$")) {
            return target.getClass().getSuperclass();
        } else {
            return target != null ? target.getClass() : null;
        }
    }
}

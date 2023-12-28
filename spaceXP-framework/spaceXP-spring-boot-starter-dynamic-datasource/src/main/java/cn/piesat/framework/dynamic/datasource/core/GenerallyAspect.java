package cn.piesat.framework.dynamic.datasource.core;

import cn.piesat.framework.dynamic.datasource.utils.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <p/>
 * {@code @description}  :一般的Aspect
 * 在启动类中增加@ImportResource("classpath:applicationContext.xml")
 * applicationContext.xml文件如下
 * <beans xmlns="http://www.springframework.org/schema/beans"
 *        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *        xmlns:aop="http://www.springframework.org/schema/aop"
 *        xsi:schemaLocation="http://www.springframework.org/schema/beans
 *                            http://www.springframework.org/schema/beans/spring-beans.xsd
 *                            http://www.springframework.org/schema/aop
 *                            http://www.springframework.org/schema/aop/spring-aop.xsd">
 *
 *     <!-- 配置切面 -->
 *     <bean id="myAspect" class="cn.piesat.framework.dynamic.datasource.core.GenerallyAspect" />
 *
 *     <!-- 配置 AOP -->
 *     <aop:config>
 *         <!-- 配置切点表达式 -->
 *         <aop:pointcut id="myPointcut" expression="execution(* cn.piesat.dynamic.datasource.service.*.*(..))" />
 *
 *         <!-- 配置切面 -->
 *         <aop:aspect ref="myAspect">
 *             <!-- 配置 Around advice -->
 *             <aop:around method="aroundAdvice" pointcut-ref="myPointcut" />
 *         </aop:aspect>
 *     </aop:config>
 *
 * </beans>
 * <p/>
 * <b>@create:</b> 2023/12/28 13:52.
 *
 * @author zhouxp
 */
public class GenerallyAspect {
    public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
        DataSourceContextHolder.setDataSource("__master");
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.removeDataSource();
        }
    }
}

package cn.piesat.framework.redis.core;

import cn.piesat.framework.redis.annotation.DLock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :分布锁切面实现类
 * <p/>
 * <b>@create:</b> 2024-04-17 13:54.
 *
 * @author zhouxp
 */
@Aspect
@Slf4j
public class DistributedLockAspect {


    private final  RedissonClient redissonClient;
    /**
     * SpEL表达式解析
     */
    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    /**
     * 用于获取方法参数名字
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut("@annotation(cn.piesat.framework.redis.annotation.DLock)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object lockAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DLock lock = method.getAnnotation(DLock.class);
        Object[] args = pjp.getArgs();

        String key = lock.value();
        if ("".equals(key)) {
            // 根据当前的类名+方法参数信息生成key
            key = configKey(signature.getDeclaringType(), method).replaceAll("[^a-zA-Z0-9]", "");
        } else {
            // 支持SpEL表达式
            StandardEvaluationContext context = new StandardEvaluationContext();
            // 这里将当前执行的方法参数信息都存入到SpEL执行的上下文中
            String[] parameterNames = nameDiscoverer.getParameterNames(method);
            for (int i = 0, len = Objects.requireNonNull(parameterNames).length; i < len; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
            Expression expression = spelExpressionParser.parseExpression(key);
            key = expression.getValue(context, String.class);
        }
        RLock rLock = redissonClient.getLock(key);
        rLock.lock();
        try {
            return pjp.proceed();
        } finally {
            rLock.unlock();
        }
    }

    // 根据类，方法信息生成分布式锁的key
    private String configKey(Class<?> targetType, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(targetType.getSimpleName());
        builder.append('#').append(method.getName()).append('(');
        for (Class<?> param : method.getParameterTypes()) {
            builder.append(param.getSimpleName()).append(',');
        }
        if (method.getParameterTypes().length > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.append(')').toString();
    }
}

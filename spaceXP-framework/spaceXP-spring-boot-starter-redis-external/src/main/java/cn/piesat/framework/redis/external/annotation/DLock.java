package cn.piesat.framework.redis.external.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * {@code @description}  :分布锁注解
 * <p/>
 * <b>@create:</b> 2024-04-17 13:53.
 *
 * @author zhouxp
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DLock {
    /**
     * 保证业务接口的key的唯一性，否则失去了分布式锁的意义 锁key
     * 支持使用spEl表达式
     */
    String value() default "";

    /**
     * 保证业务接口的key的唯一性，否则失去了分布式锁的意义 锁key 前缀
     */
    String keyPrefix() default "";

    /**
     * 是否在等待时间内获取锁，如果在等待时间内无法获取到锁，则返回失败
     */
    boolean tryLok() default false;

    /**
     * 获取锁的最大尝试时间 ，会尝试tryTime时间获取锁，在该时间内获取成功则返回，否则抛出获取锁超时异常，tryLok=true时，该值必须大于0。
     *
     */
    long tryTime() default 0;

    /**
     * 加锁的时间，超过这个时间后锁便自动解锁
     */
    long lockTime() default 30;

    /**
     * tryTime 和 lockTime的时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 是否公平锁，false:非公平锁，true:公平锁
     */
    boolean fair() default false;

}

package cn.piesat.framework.redis.core;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.redis.annotation.AccessLimit;

import org.springframework.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * <p/>
 * {@code @description}  : 限流拦截器
 * 通过路径:ip的作为key，访问次数为value的方式对某一用户的某一请求进行唯一标识
 * * 每次访问的时候判断key是否存在，是否count超过了限制的访问次数
 * <p/>
 * <b>@create:</b> 2023/12/19 16:01.
 *
 * @author zhouxp
 */
@Slf4j

public class AccessLimitInterceptor implements HandlerInterceptor {
    private final RedisService redisService;

    private final String keyPrefix;

    public AccessLimitInterceptor(RedisService redisService, String keyPrefix) {
        this.redisService = redisService;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)  {

        if (handler instanceof HandlerMethod ) {
            // 强转
            // 获取方法
            HandlerMethod handlerMethod  = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 是否有AccessLimit注解
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }
            // 获取注解内容信息
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            long seconds = accessLimit.second();
            int maxCount = accessLimit.maxCount();

            // 存储key
            String key = keyPrefix + CommonConstants.UNDERLINE + request.getRemoteAddr().replace(CommonConstants.COLON, CommonConstants.UNDERLINE) +
                    CommonConstants.UNDERLINE +
                    request.getServletPath();

            // 已经访问的次数
            Integer count = (Integer) redisService.redisTemplate.opsForValue().get(key);
            log.info("已经访问的次数:" + count);
            if (null == count || -1 == count) {
                redisService.setObject(key, 1, seconds, TimeUnit.SECONDS);
                return true;
            }

            if (count < maxCount) {
                redisService.redisTemplate.opsForValue().increment(key);
                return true;
            }
            throw new BaseException("请求过于频繁请稍后再试");
        }
        return true;
    }
}



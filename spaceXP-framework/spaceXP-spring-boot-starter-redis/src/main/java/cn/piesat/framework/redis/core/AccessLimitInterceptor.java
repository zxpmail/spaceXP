package cn.piesat.framework.redis.core;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.redis.annotation.AccessLimit;


import cn.piesat.framework.redis.properties.RedisProperties;
import cn.piesat.framework.redis.utils.Md5Util;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private final RedisProperties  redisProperties;

    private final DefaultRedisScript<Long> redisLuaScript = new DefaultRedisScript<>();


    public AccessLimitInterceptor( RedisProperties  redisProperties) {
        this.redisProperties = redisProperties;
    }

    @PostConstruct
    public void init() {
        redisLuaScript.setResultType(Long.class);
        redisLuaScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimiter.lua")));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {

        if (handler instanceof HandlerMethod) {
            // 强转
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法
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
            String params = "";
            if(!redisProperties.getOnlyUrl()) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (!CollectionUtils.isEmpty(parameterMap)) {
                    String s = JSON.toJSONString(parameterMap);
                    params = Md5Util.generateMD5(s);
                }
            }
            // 存储key
            String key = redisProperties.getKeyPrefix() +
                    CommonConstants.UNDERLINE +
                    request.getRequestURI() + params;
            List<String> keys = new ArrayList<>();
            keys.add(key);
            Long count = stringRedisTemplate.execute(
                    redisLuaScript,
                    keys,
                    String.valueOf(maxCount),
                    String.valueOf(seconds));
            log.info("已经访问的次数:{},key:{}" , count, key);
            if (count != null && count == 0) {
                log.info("限流功能key:{} ", key);
                throw new BaseException(accessLimit.message());
            }
        }
        return true;
    }
}



package cn.piesat.framework.sse.model;

import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: SSE属性类
 * <p/>
 * {@code @create}: 2024-09-26 13:05
 * {@code @author}: zhouxp
 */
@Data
public class SseAttributes {
    /**
     * 用户会话
     */
    private SseEmitter session;
    /**
     * 属性
     */
    private Map<String, Object> attributes =new HashMap<>();
}

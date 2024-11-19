package cn.piesat.tools.gateway.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 日志实体类
 * <p/>
 * {@code @create}: 2024-11-19 16:34
 * {@code @author}: zhouxp
 */
@Data
public class GatewayLog implements Serializable {

    /**
     * 访问实例
     */
    private String targetServer;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求与方法
     */
    private String method;

    /**
     * 请求协议
     */
    private String schema;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 请求参数
     */
    private Map<String,String> queryParams;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 请求执行时间
     */
    private Long executeTime;

    /**
     * 请求类型
     */
    private String requestContentType;

    /**
     * 相应状态码
     */
    private int code;
}


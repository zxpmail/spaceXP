package cn.piesat.framework.common.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p/>
 * {@code @description}  :日志记录实体类
 * <p/>
 * <b>@create:</b> 2022/11/28 9:00.
 *
 * @author zhouxp
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OpLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    /**
     * 系统模块
     */
    private String module;
    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门
     */
    private String deptName;

    /**
     * 业务操作
     */
    private String op;
    /**
     * 业务代码
     */
    private  Integer code;
    /**
     * 操作IP
     */
    private String ip;

    /**
     * 操作地点
     */
    private String location;

    /**
     * 操作类型 1 操作记录 2异常记录
     */
    private Integer type;

    /**
     * 操作人ID
     */
    private String userName;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求方法
     */
    private String actionMethod;

    /**
     * 请求url
     */
    private String actionUrl;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 消耗时间
     */
    private Long consumingTime;

    /**
     * 异常详情信息 堆栈信息
     */
    private String exDetail;

    /**
     * 异常描述 e.getMessage
     */
    private String exDesc;


    /**
     * headerSelfDefineValue
     */
    private String headerSelfDefineValue;

    /**
     * request信息
     */

    private String requestInfo;
    /**
     * 响应数据
     */
    private String responseData;

    @JsonIgnore
    private HttpServletRequest request;
}

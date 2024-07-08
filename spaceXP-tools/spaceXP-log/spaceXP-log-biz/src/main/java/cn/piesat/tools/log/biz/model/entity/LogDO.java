package cn.piesat.tools.log.biz.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 执行日志
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:01
 */
@Data
@TableName("sys_log")
@ApiModel("执行日志实体类")
public class LogDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 主键自增
    */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("主键自增")
    private Long id;
    /**
    * 模块名称
    */
    @ApiModelProperty("模块名称")
    private String module;
    /**
    * 接口名称
    */
    @ApiModelProperty("接口名称")
    private String description;
    /**
    * 接口方法
    */
    @ApiModelProperty("接口方法")
    private String actionMethod;
    /**
    * 接口地址
    */
    @ApiModelProperty("接口地址")
    private String actionUrl;
    /**
    * 类路径
    */
    @ApiModelProperty("类路径")
    private String classPath;
    /**
    * 请求方式
    */
    @ApiModelProperty("请求方式")
    private String requestMethod;
    /**
    * 请求IP地址
    */
    @ApiModelProperty("请求IP地址")
    private String ip;
    /**
    * 操作系统
    */
    @ApiModelProperty("操作系统")
    private String os;
    /**
    * 浏览器
    */
    @ApiModelProperty("浏览器")
    private String browser;
    /**
    * 请求参数
    */
    @ApiModelProperty("请求参数")
    private String params;
    /**
    * 开始时间
    */
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    /**
    * 完成时间
    */
    @ApiModelProperty("完成时间")
    private LocalDateTime finishTime;
    /**
    * 请求编号
    */
    @ApiModelProperty("请求编号")
    private String requestInfo;
    /**
    * 响应数据
    */
    @ApiModelProperty("响应数据")
    private String responseData;
    /**
    * 异常详情
    */
    @ApiModelProperty("异常详情")
    private String exDetail;
    /**
    * 异常描述
    */
    @ApiModelProperty("异常描述")
    private String exDesc;
    /**
    * 类型：1 操作记录 2异常记录
    */
    @ApiModelProperty("类型：1 操作记录 2异常记录")
    private Integer type;
    /**
    * 部门ID
    */
    @ApiModelProperty("部门ID")
    @TableField(fill = FieldFill.INSERT)
    private Long deptId;
    /**
    * 分系统标识
    */
    @ApiModelProperty("分系统标识")
    private String subSystem;
    /**
    * 租户编号
    */
    @ApiModelProperty("租户编号")
    private Long tenantId;
    /**
    * 创建者
    */
    @ApiModelProperty("创建者")
    private Long creator;
    /**
    * 创建日期
    */
    @ApiModelProperty("创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 耗时
     */
    @ApiModelProperty("耗时")
    private Long consumingTime;
    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String userName;
}

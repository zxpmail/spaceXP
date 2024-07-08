package cn.piesat.tools.log.api.model.dto;


import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 执行日志DTO
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:01
 */
@Data
@ApiModel("执行日志DTO")
public class LogDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键自增
    */
    @NotNull(message = "主键不能为空", groups = UpdateGroup.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("主键自增")
    private Long id;
    /**
     * 模块名称
    */
    @ApiModelProperty("模块名称")
    @NotBlank(message = "模块名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String module;
    /**
     * 接口名称
    */
    @ApiModelProperty("接口名称")
    @NotBlank(message = "接口名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String description;
    /**
     * 接口方法
    */
    @ApiModelProperty("接口方法")
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String actionMethod;
    /**
     * 接口地址
    */
    @ApiModelProperty("接口地址")
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String actionUrl;
    /**
     * 类路径
    */
    @ApiModelProperty("类路径")
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String classPath;
    /**
     * 请求方式
    */
    @ApiModelProperty("请求方式")
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String requestMethod;
    /**
     * 请求IP地址
    */
    @ApiModelProperty("请求IP地址")
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String ip;
    /**
     * 操作系统
    */
    @ApiModelProperty("操作系统")
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String os;
    /**
     * 浏览器
    */
    @ApiModelProperty("浏览器")
    @NotBlank(message = "浏览器不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
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
    @Length(max = 500 , message = "长度必须小于等于500" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String requestId;
    /**
     * 响应数据
    */
    @ApiModelProperty("响应数据")
    private String responseData;
    /**
     * 异常详情
    */
    @ApiModelProperty("异常详情")
    private String exceptionDetail;
    /**
     * 异常描述
    */
    @ApiModelProperty("异常描述")
    private String exceptionDesc;
    /**
     * 类型：1 操作记录 2异常记录
    */
    @ApiModelProperty("类型：1 操作记录 2异常记录")
    private Integer type;
    /**
     * 部门ID
    */
    @ApiModelProperty("部门ID")
    @NotNull(message = "部门ID不能为空", groups = UpdateGroup.class)
    private Long deptId;
}

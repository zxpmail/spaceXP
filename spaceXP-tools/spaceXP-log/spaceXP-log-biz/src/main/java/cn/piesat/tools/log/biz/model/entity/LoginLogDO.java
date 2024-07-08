package cn.piesat.tools.log.biz.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统访问记录
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:02
 */
@Data
@TableName("sys_login_log")
@ApiModel("系统访问记录实体类")
public class LoginLogDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 访问ID
    */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("访问ID")
    private Long id;
    /**
    * 用户ID
    */
    @ApiModelProperty("用户ID")
    private Long userId;
    /**
    * 用户账号
    */
    @ApiModelProperty("用户账号")
    private String userName;
    /**
    * 登录IP地址
    */
    @ApiModelProperty("登录IP地址")
    private String ip;
    /**
    * 浏览器类型
    */
    @ApiModelProperty("浏览器类型")
    private String browser;
    /**
    * 操作系统
    */
    @ApiModelProperty("操作系统")
    private String os;
    /**
    * 登录状态（0成功 1失败）
    */
    @ApiModelProperty("登录状态（0成功 1失败）")
    private Integer type;
    /**
    * 提示消息
    */
    @ApiModelProperty("提示消息")
    private String msg;
    /**
    * 是否登录0登录1退出
    */
    @ApiModelProperty("是否登录0登录1退出")
    private Integer logined;
    /**
    * 访问时间
    */
    @ApiModelProperty("访问时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
    * 租户编号
    */
    @ApiModelProperty("租户编号")
    private Long tenantId;
}

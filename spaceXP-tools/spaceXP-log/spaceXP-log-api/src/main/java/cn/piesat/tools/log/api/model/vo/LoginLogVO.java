package cn.piesat.tools.log.api.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统访问记录VO
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:02
 */
@Data
@ApiModel("系统访问记录VO")
public class LoginLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 访问ID
     */

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("访问ID")
    private Long id;

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
     * 登录状态（0成功 1失败）
     */
    @ApiModelProperty("登录状态（1 操作记录 2异常记录）")
    private Integer type;

    /**
     * 是否登录0登录1退出
     */
    @ApiModelProperty("是否登录0登录1退出")
    private Integer logined;
    /**
     * 提示消息
     */
    @ApiModelProperty("提示消息")
    private String msg;
    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("访问时间")
    private LocalDateTime createTime;
}

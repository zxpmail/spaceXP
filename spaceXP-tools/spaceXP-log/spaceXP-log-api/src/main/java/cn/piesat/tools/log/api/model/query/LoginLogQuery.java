package cn.piesat.tools.log.api.model.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统访问记录Query
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:02
 */
@Data
@ApiModel("系统访问记录查询对象")
public class LoginLogQuery {
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
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}

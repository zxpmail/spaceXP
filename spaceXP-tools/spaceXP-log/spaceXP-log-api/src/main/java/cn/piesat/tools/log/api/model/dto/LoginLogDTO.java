package cn.piesat.tools.log.api.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统访问记录DTO
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:02
 */
@Data
@ApiModel("系统访问记录DTO")
public class LoginLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 提示消息
    */
    @ApiModelProperty("提示消息")
    private String msg;
}

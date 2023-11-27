package cn.piesat.multi.tenant.model.entity;



import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-01-16 08:52:49
 */
@Data
@ApiModel("用户信息实体类")
@TableName("sys_user")
public class UserDO implements Serializable {
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long id;
    @NotBlank(message = "用户名不能为空！",groups = AddGroup.class)
    private String username;
    private String password;
    @NotBlank(message = "别名不能为空！" ,groups = AddGroup.class)

    private String nickname;
    private String sex;
    private Long state;
    private String changePasswordFlag;
    private LocalDateTime passwordUpdateTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long deptId;

    @TableLogic
    private Integer delFlag ;
}

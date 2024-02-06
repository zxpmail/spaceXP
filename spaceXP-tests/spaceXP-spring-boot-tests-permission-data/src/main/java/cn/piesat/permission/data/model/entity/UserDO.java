package cn.piesat.permission.data.model.entity;



import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;


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

@TableName("sys_user")
public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String password;


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

    private Long deptId;

    @TableLogic
    private Integer delFlag ;
}

package cn.piesat.tests.mybaits.plus.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
@Data
@Schema(description = "实体类")
@TableName("sys_user")
public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String sex;
    private Long state;
    private Long deptId;
    private String changePasswordFlag;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime passwordUpdateTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
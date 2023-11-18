package cn.piesat.tests.feign.consumer.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
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
@ApiModel("实体类")

public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using= ToStringSerializer.class)

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String sex;
    private Long state;
    private Long deptId;
    private String changePasswordFlag;

    private LocalDateTime passwordUpdateTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
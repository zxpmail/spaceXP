package cn.piesat.tests.test.doman;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * <p/>
 * {@code @description}: 用户DTO
 * <p/>
 * {@code @create}: 2024-08-13 10:03
 * {@code @author}: zhouxp
 */
@Data
public  class UserDTO {

    @Max(4)
    @Min(1)
    private Integer age;
    @Length(min = 2, max = 40)
    private String name;
    @Email
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;

    private Boolean deleted;
}

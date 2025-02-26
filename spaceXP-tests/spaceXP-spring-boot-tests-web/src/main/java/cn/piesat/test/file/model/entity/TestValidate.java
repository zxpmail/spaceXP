package cn.piesat.test.file.model.entity;

import cn.piesat.framework.web.annotation.ConditionalValidateField;
import cn.piesat.framework.web.constants.Constants;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-26 19:14:18
 * {@code @author}: zhouxp
 */
@Data
public class TestValidate {
    /**
     * 姓名
     **/
    @NotBlank(message = "姓名必填")
    private String name;

    /**
     *  是否有房 0 - 没房子 1 - 有房子
     **/
    @NotNull(message = "是否有房必填")
    @ConditionalValidateField(relationField = "hoursAreas", ifValue = "1",
            action = Constants.IF_EQ_NOT_NULL,
            message = "有房子，房子面积必填")
    @ConditionalValidateField(relationField = "time", ifValue = "0",
            action = Constants.IF_EQ_NOT_NULL,
            message = "没房子，要填准备买房日期")
    private Integer isHaveHours;

    /***
     * 房子面积
     **/
    private Integer hoursAreas;


    /**
     * 什么时候买房
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

}

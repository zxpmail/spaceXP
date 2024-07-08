package cn.piesat.log.api.model.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 执行日志Query
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:01
 */
@Data
@ApiModel("执行日志查询对象")
public class LogQuery {
    /**
     * 模块名称
     */
    @ApiModelProperty("模块名称")
    private String module;
    /**
     * 类型：1 操作记录 2异常记录
     */
    @ApiModelProperty("类型：1 操作记录 2异常记录")
    private Integer type;
    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long deptId;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}

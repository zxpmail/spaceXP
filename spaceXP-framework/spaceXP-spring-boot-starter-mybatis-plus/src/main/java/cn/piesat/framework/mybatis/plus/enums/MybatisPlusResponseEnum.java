package cn.piesat.framework.mybatis.plus.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 * @author :zhouxp
 * {@code @date} 2022/9/30 16:12
 * {@code @description} :
 */
@Getter
@AllArgsConstructor
public enum MybatisPlusResponseEnum implements IBaseResponse {
    BAD_SQL_GRAMMAR_ERROR(503,"不支持当前数据库"),
    RECORD_REPEAT(504,"数据库中已存在该记录"),

    QUERY_DATA(505,"数据处理错误！"),

    ;

    /**
     * 响应状态码
     */
    private final Integer code;

    /**
     * 响应信息
     */
    private final   String message;
}

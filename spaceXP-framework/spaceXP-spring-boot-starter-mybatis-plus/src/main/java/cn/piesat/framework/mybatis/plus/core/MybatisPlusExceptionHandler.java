package cn.piesat.framework.mybatis.plus.core;


import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.mybatis.plus.enums.MybatisPlusResponseEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>统一WEB异常处理类</p>
 *
 * @author :zhouxp
 * {@code @date} 2022/9/30 15:44
 * {@code @description} :
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class MybatisPlusExceptionHandler {
    private final String module;

    @ExceptionHandler(value = BadSqlGrammarException.class)
    public ApiResult<IBaseResponse> handleException(BadSqlGrammarException e) {
        log.error(CommonConstants.MESSAGE,module,e.getMessage());
        return ApiResult.fail(MybatisPlusResponseEnum.BAD_SQL_GRAMMAR_ERROR);
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResult<IBaseResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(CommonConstants.MESSAGE,module,e.getMessage());
        return ApiResult.fail(MybatisPlusResponseEnum.RECORD_REPEAT);
    }
}

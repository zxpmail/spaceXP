package cn.piesat.framework.mybatis.plus.core;


import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.model.vo.ApiMapResult;
import cn.piesat.framework.common.utils.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
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
    public ApiMapResult<IBaseResponse> handleException(BadSqlGrammarException e) {
        log.error(CommonConstants.MESSAGE,module, ExceptionUtil.getMessage(e));
        return ApiMapResult.fail(CommonResponseEnum.BAD_SQL_GRAMMAR_ERROR);
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public ApiMapResult<IBaseResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(CommonConstants.MESSAGE,module, ExceptionUtil.getMessage(e));
        return ApiMapResult.fail(CommonResponseEnum.RECORD_REPEAT);
    }

    @ExceptionHandler(PersistenceException.class)
    public ApiMapResult<IBaseResponse> handlePersistenceException(PersistenceException e) {
        String message = e.getMessage();
        log.error(CommonConstants.MESSAGE,module,ExceptionUtil.getMessage(e));
        if(message.contains(CommonResponseEnum.NO_PERMISSION_DATA.getMessage())){
            return ApiMapResult.fail(CommonResponseEnum.NO_PERMISSION_DATA);
        }else{
            return ApiMapResult.fail(CommonResponseEnum.QUERY_DATA);
        }
    }
}

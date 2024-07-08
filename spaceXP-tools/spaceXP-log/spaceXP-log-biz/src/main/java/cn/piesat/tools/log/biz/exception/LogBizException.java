package cn.piesat.tools.log.biz.exception;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;

/**
 * biz异常类
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-04 13:38:06
 */
public class LogBizException extends BaseException {
    /**
     * 所属模块
     * @param iBaseResponse 通用响应类
     */
    public LogBizException(IBaseResponse iBaseResponse) {
        super(iBaseResponse);
    }
}

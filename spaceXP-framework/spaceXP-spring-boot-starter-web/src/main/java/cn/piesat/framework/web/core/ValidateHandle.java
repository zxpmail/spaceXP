package cn.piesat.framework.web.core;

import cn.piesat.framework.web.annotation.ConditionalValidateField;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.Map;

/**
 * <p/>
 * {@code @description}: 校验处理接口
 * <p/>
 * {@code @create}: 2025-02-26 16:26:54
 * {@code @author}: zhouxp
 */
public interface ValidateHandle {
    /**
     * 校验处理
     *
     * @param conditionalValidateField       条件校验注解
     * @param fieldClzMap                    字段类型
     * @param parser                         表达式解析器
     * @param conditionalValidateFieldInfo   条件校验信息
     * @param context                        上下文
     * @param paramsName                     参数名称
     */
    void doValidate(ConditionalValidateField conditionalValidateField, Map<String, Class<?>> fieldClzMap, ExpressionParser parser,
                    ConditionalValidateFieldInfo conditionalValidateFieldInfo, EvaluationContext context, String[] paramsName);
}

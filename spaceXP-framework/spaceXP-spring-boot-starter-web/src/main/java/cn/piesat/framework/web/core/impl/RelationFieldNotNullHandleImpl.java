package cn.piesat.framework.web.core.impl;

import cn.piesat.framework.web.annotation.ConditionalValidateField;
import cn.piesat.framework.web.core.ConditionalValidateFieldInfo;
import cn.piesat.framework.web.core.ValidateHandle;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}: 关联字段不为空处理器
 * <p/>
 * {@code @create}: 2025-02-26 16:39:04
 * {@code @author}: zhouxp
 */
public class RelationFieldNotNullHandleImpl implements ValidateHandle {
    @Override
    public void doValidate(ConditionalValidateField conditionalValidateField, Map<String, Class<?>> fieldClzMap,
                           ExpressionParser parser, ConditionalValidateFieldInfo conditionalValidateFieldInfo,
                           EvaluationContext context, String[] paramsName) {
        String fieldName = conditionalValidateFieldInfo.getFieldName();
        Class<?> originalClz = fieldClzMap.get(fieldName);

        if (originalClz == null) {
            throw new IllegalArgumentException("Field " + fieldName + " not found in fieldClzMap");
        }

        try {
            Expression expression = parser.parseExpression("#" + paramsName[0] + "." + fieldName);
            Object originalValue = expression.getValue(context, originalClz);

            if (StringUtils.hasText(conditionalValidateField.ifValue())) {
                // 如果是相等的
                if (convertToType(originalClz, conditionalValidateField.ifValue()).equals(originalValue)) {
                    validateRelationField(conditionalValidateField, parser, context, paramsName, fieldClzMap);
                }
            } else {
                // 为空的情况,有可能要求原字段为空，关联字段不能为空的情况；判断都是空就校验
                if (Objects.isNull(originalValue)) {
                    validateRelationField(conditionalValidateField, parser, context, paramsName, fieldClzMap);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during validation for field " + fieldName, e);
        }
    }

    private void validateRelationField(ConditionalValidateField conditionalValidateField, ExpressionParser parser,
                                       EvaluationContext context, String[] paramsName, Map<String, Class<?>> fieldClzMap) {
        String relationField = conditionalValidateField.relationField();
        Class<?> relationClz = fieldClzMap.get(relationField);

        if (relationClz == null) {
            throw new IllegalArgumentException("Relation field " + relationField + " not found in fieldClzMap");
        }

        Expression relationExpression = parser.parseExpression("#" + paramsName[0] + "." + relationField);
        Object value = relationExpression.getValue(context, relationClz);

        Assert.isTrue(StringUtils.hasText(String.valueOf(value)), conditionalValidateField.message());
    }

    private Object convertToType(Class<?> clz, String value) {
        if (clz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clz == String.class) {
            return value;
        } else if (clz == Boolean.class) {
            return Boolean.valueOf(value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + clz.getName());
        }
    }
}

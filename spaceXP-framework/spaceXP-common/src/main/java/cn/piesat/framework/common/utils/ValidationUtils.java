package cn.piesat.framework.common.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.StringJoiner;

/**
 * <p/>
 * {@code @description}: 校验工具类
 * <p/>
 * {@code @create}: 2024-09-06 16:08
 * {@code @author}: zhouxp
 */
public class ValidationUtils {
    private static final Validator validator;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * 校验对象
     */
    public static void validate(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            String s = validationResult(violations);
            throw new RuntimeException(s);
        }
    }

    /**
     * 获取校验失败的消息
     */
    public static <T> Set<ConstraintViolation<T>> getValidationMessages(T bean, Class<?>... groups) {
        return validator.validate(bean, groups);
    }

    /**
     * 包装校验结果
     */
    private static <T> String validationResult(Set<ConstraintViolation<T>> constraintViolations) {
        if (constraintViolations.isEmpty()) {
            return "";
        }
        StringJoiner sj = new StringJoiner(";");
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            sj.add(constraintViolation.getMessage());
        }
        return sj.toString();
    }
}

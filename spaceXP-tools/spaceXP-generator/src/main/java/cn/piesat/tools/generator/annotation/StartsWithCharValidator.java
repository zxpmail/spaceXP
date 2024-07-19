package cn.piesat.tools.generator.annotation;

import cn.piesat.tools.generator.utils.TemplateDataUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p/>
 * {@code @description}: 校验是否以字符开始
 * <p/>
 * {@code @create}: 2024-07-19 15:23
 * {@code @author}: zhouxp
 */
public class StartsWithCharValidator implements ConstraintValidator<StartsWithChar, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) {
            return true; // 或者根据你的需求来处理null值
        }
        return !s.isEmpty() && TemplateDataUtils.isChar(s.charAt(0)) ;
    }
}

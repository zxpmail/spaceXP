package cn.piesat.framework.web.validator;

import cn.piesat.framework.web.validator.annotation.MobilePhone;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * {@code @description}:手机注解校验器
 * <p/>
 * {@code @create}: 2024-12-23 13:42
 * {@code @author}: zhouxp
 */
public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, CharSequence> {
    private boolean required = false;

    private final Pattern pattern = Pattern.compile("^1[34578][0-9]{9}$");  //  验证手机号

    /**
     * 在验证开始前调用注解里的方法，从而获取到一些注解里的参数
     */
    @Override
    public void initialize(MobilePhone constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (this.required) {
            return isMobilePhone(value);
        }
        if (StringUtils.hasText(value)) {
            return isMobilePhone(value);
        }
        return true;
    }

    private boolean isMobilePhone(final CharSequence str) {
        Matcher m = pattern.matcher(str);
        return m.matches();
    }
}

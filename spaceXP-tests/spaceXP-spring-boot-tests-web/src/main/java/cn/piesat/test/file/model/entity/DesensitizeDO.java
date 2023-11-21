package cn.piesat.test.file.model.entity;

import cn.piesat.framework.security.desensitize.annotation.CustomDesensitize;
import cn.piesat.framework.security.desensitize.enums.DesensitizeRuleEnums;
import lombok.Data;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/5/4 17:07.
 *
 * @author zhouxp
 */
@Data
public class DesensitizeDO {
    // 姓名
    @cn.piesat.framework.security.desensitize.annotation.Desensitize(rule = DesensitizeRuleEnums.CHINESE_NAME)
    private String name;

    // 邮箱
    @cn.piesat.framework.security.desensitize.annotation.Desensitize(rule = DesensitizeRuleEnums.EMAIL)
    private String email;

    // 电话
    @cn.piesat.framework.security.desensitize.annotation.Desensitize(rule = DesensitizeRuleEnums.MOBILE_PHONE)
    private String phone;

    @CustomDesensitize(start = 1,end = 2)
    private String custom;
}

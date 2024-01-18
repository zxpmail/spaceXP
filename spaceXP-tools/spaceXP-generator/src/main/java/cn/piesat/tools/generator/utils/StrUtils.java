package cn.piesat.tools.generator.utils;

import cn.piesat.tools.generator.constants.Constants;
import org.springframework.util.StringUtils;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024/1/8 19:58.
 *
 * @author zhouxp
 */
public class StrUtils {

    /**
     * 下划线字符串转成驼峰
     * @param param 划线字符串
     * @param capitalize true表示首字母为大写 false 表示首字母为小写
     * @return 转换为驼峰字符串
     */
    public static String underlineToCamel(String param,boolean capitalize) {
        if (param == null || param.trim().isEmpty()) {
            return Constants.EMPTY;
        }
        StringBuilder sb = new StringBuilder(param.length());
        if(capitalize) {
            sb.append(Character.toUpperCase(param.charAt(0)));
        }else {
            sb.append(param.charAt(0));
        }
        for (int i = 1; i < param.length(); i++) {
            char c = param.charAt(i);
            if (c == Constants.UNDERLINE_CHAR) {
                sb.append(Character.toUpperCase(param.charAt(++i)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

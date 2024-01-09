package cn.piesat.tools.generator.utils;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
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
    public static String underlineToCamel(String param) {
        if (!StringUtils.hasText(param)) {
            return StringPool.EMPTY;
        }
        String temp = param.toLowerCase();
        int len = temp.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = temp.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(temp.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

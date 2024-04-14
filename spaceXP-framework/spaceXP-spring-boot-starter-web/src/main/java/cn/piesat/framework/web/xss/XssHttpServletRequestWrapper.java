
package cn.piesat.framework.web.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(StringUtils.hasText(value)) {
            return HtmlUtils.htmlEscape(value);
        }
        return super.getHeader(name);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(StringUtils.hasText(value)) {
            return HtmlUtils.htmlEscape(value);
        }
        return super.getParameter(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapeValues[i] = HtmlUtils.htmlEscape(values[i]).trim();
            }
            return escapeValues;
        }
        return super.getParameterValues(name);
    }
}

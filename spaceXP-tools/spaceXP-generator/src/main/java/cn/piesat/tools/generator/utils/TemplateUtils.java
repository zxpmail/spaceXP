package cn.piesat.tools.generator.utils;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.io.StringWriter;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :模版工具类
 * <p/>
 * <b>@create:</b> 2023/12/29 14:24.
 *
 * @author zhouxp
 */
@Slf4j
public class TemplateUtils {
    public static String getContent(String content, Map<String, Object> dataModel) {
        if (dataModel.isEmpty()) {
            return content;
        }
        String templateName="";
        try (StringReader reader = new StringReader(content);
             StringWriter sw = new StringWriter()) {
            templateName = (String) dataModel.get("templateName");
            Template template = new Template(templateName, reader, null, StandardCharsets.UTF_8.name());
            template.process(dataModel, sw);
            return sw.toString();
        } catch (Exception e) {
            log.error("渲染模板失败：{} ,error {}",templateName,  e.getMessage(), e);
            throw new RuntimeException("渲染模板失败，请检查模板语法和数据模型", e);
        }
    }
}

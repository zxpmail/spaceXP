package cn.piesat.tools.generator.utils;

import cn.piesat.tools.generator.model.GeneratorInfo;
import cn.piesat.tools.generator.model.TemplateInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  : 从配置中获取配置
 * <p/>
 * <b>@create:</b> 2023/12/29 14:29.
 *
 * @author zhouxp
 */
public class ConfigUtils {

    private static final GeneratorInfo GENERATOR_INFO = new GeneratorInfo();

    @SneakyThrows
    public static synchronized void init() {
        if(!Objects.isNull(GENERATOR_INFO.getProject())){
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream isConfig = ConfigUtils.class.getResourceAsStream("/template/config.json");// 模板配置文件
        if (isConfig == null) {
            throw new RuntimeException("模板配置文件，config.json不存在");
        }
        GeneratorInfo generator = objectMapper.readValue(isConfig, GeneratorInfo.class);
        // 读取模板配置文件
        for (TemplateInfo templateInfo : generator.getTemplates()) {
            // 模板文件
            InputStream isTemplate = ConfigUtils.class.getResourceAsStream(templateInfo.getTemplateName());
            if (isTemplate == null) {
                continue;
            }
            // 读取模板内容
            String templateContent = StreamUtils.copyToString(isTemplate, StandardCharsets.UTF_8);
            templateInfo.setTemplateContent(templateContent);
        }
        GENERATOR_INFO.setProject(generator.getProject());
        GENERATOR_INFO.setTemplates(generator.getTemplates());
    }

    public static GeneratorInfo getGeneratorInfo(){
        return GENERATOR_INFO;
    }
}

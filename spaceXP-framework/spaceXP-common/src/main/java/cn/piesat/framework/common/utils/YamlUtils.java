package cn.piesat.framework.common.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: yaml文件解析工具
 * <p/>
 * {@code @create}: 2025-01-14 16:16
 * {@code @author}: zhouxp
 */
public class YamlUtils {
    private static LinkedHashMap<String, Map<String, Object>> properties;

    public static void Load(String filePath) {
        try (InputStream in = YamlUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            Yaml yaml = new Yaml();
            properties = yaml.loadAs(in, LinkedHashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration: " + e.getMessage(), e);
        }
    }

    public static Object getValueByKey(String root, String key) {
        // 空值检查
        if (root == null || key == null) {
            return null;
        }

        // 获取根属性
        Map<String, Object> rootProperty = properties.get(root);

        // 边界条件处理
        if (rootProperty == null) {
            return null;
        }

        // 如果 root 和 key 相等，直接返回对应的 value
        if (root.equals(key)) {
            return rootProperty.toString();
        }

        // 否则递归查找子属性
        return iter((LinkedHashMap<String, Object>) rootProperty, key);
    }

    @SuppressWarnings("unchecked")
    private static Object iter(LinkedHashMap<String, Object> rootProperty, String key) {
        try {
            if (rootProperty.containsKey(key)) {
                return rootProperty.get(key);
            }
            // 遍历所有 entry
            for (Map.Entry<String, Object> entry : rootProperty.entrySet()) {
                if (entry.getValue() instanceof LinkedHashMap) {
                    return iter((LinkedHashMap<String, Object>) entry.getValue(), key);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("FError occurred during iteration: " + e.getMessage(), e);
        }
        return null;
    }
}

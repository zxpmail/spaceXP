package cn.piesat.tools.generator.utils;


import cn.piesat.tools.generator.model.dto.TableDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Map;

import static cn.piesat.tools.generator.constants.Constants.*;

/**
 * <p/>
 * {@code @description}: 模版数据工具类
 * <p/>
 * {@code @create}: 2024-07-18 13:35
 * {@code @author}: zhouxp
 */
@Slf4j
public class TemplateDataUtils {
    public static void setDataModelByTable(Map<String, Object> dataModel, TableDTO table) {
        putStringIfNotEmpty(dataModel,PACKAGE,table.getPackageName());
        putStringIfNotEmpty(dataModel,PACKAGE_PATH, table.getPackageName().replace(PERIOD, File.separator));
        putStringIfNotEmpty(dataModel,VERSION,table.getVersion());
        putStringIfNotEmpty(dataModel,MODULE_NAME, table.getModuleName());
        putStringIfNotEmpty(dataModel,DB_TYPE, table.getDbType());
        putStringIfNotEmpty(dataModel,AUTHOR, table.getAuthor());
        putStringIfNotEmpty(dataModel,EMAIL, table.getEmail());
        if (ObjectUtils.isEmpty(table.getGeneratorType())) {
            table.setGeneratorType(SINGLE_MODULE);
        }
        if (table.getGeneratorType() == SINGLE_MODULE) {
            putStringIfNotEmpty(dataModel,BIZ_PATH, table.getModuleName());
            putStringIfNotEmpty(dataModel,MODEL_PATH, table.getModuleName());
            putStringIfNotEmpty(dataModel,FRONT_PATH, table.getModuleName());
        } else {
            putStringIfNotEmpty(dataModel,BIZ_PATH, table.getModuleName() + "-biz");
            putStringIfNotEmpty(dataModel,BIZ_PATH, table.getModuleName() + "-model");
            putStringIfNotEmpty(dataModel,BIZ_PATH, table.getModuleName() + "-view");
        }
        String tableName = table.getTableName();
        putStringIfNotEmpty(dataModel,TABLE_NAME, tableName);
        if (StringUtils.hasText(table.getTablePrefix())) {
            String temp =tableName.substring(table.getTablePrefix().length());
            if(StringUtils.hasText(temp)||isChar(temp.charAt(0))){
                tableName =temp;
                if (table.getFunctionName().equals(table.getTableName())) {
                    table.setFunctionName(tableName);
                }
            }
        }
        tableName = StrUtils.underlineToCamel(tableName, false);
        putStringIfNotEmpty(dataModel,TABLE_NAME, tableName);

        putStringIfNotEmpty(dataModel,TABLE_COMMENT, changeNameFirst(table.getTableComment(),tableName) );
        putStringIfNotEmpty(dataModel,CLASS_NAME, changeNameFirst(table.getClassName(),tableName) );
        putStringIfNotEmpty(dataModel,FUNCTION_NAME, changeNameFirst(table.getFunctionName(),tableName) );
    }

    /**
     * 改变第一个字符串首字母小写，如果第一为空 默认第二个字符串
     */
    public static String changeNameFirst(String first, String second) {
        return StringUtils.hasText(first) ? StringUtils.uncapitalize(first) : StringUtils.uncapitalize(second);
    }

    /**
     * 填充字符串到map中
     */
    public static void putStringIfNotEmpty(Map<String, Object> map, String key, String value) {
        if (StringUtils.hasText(value)) {
            map.put(key, value);
        } else {
            log.error("key:{}的值为空！ ", key);
        }
    }
    public static boolean isChar(char c) {
        return (c >= 65 && c <= 90)||(c >= 97 && c <= 122);
    }
}

package cn.piesat.tools.generator.utils;


import cn.piesat.tools.generator.constants.Constants;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 用字段列表数据来填充模版数据
     *
     * @param dataModel   模版数据
     * @param tableFields 字段列表数据
     */
    public static void setDataModelByField(Map<String, Object> dataModel, List<TableFieldDO> tableFields) {

        Set<TableFieldDO> voList = new HashSet<>();
        Set<TableFieldDO> dtoList = new HashSet<>();
        Set<TableFieldDO> selectList = new HashSet<>();
        Set<TableFieldDO> queryList = new HashSet<>();
        Set<TableFieldDO> repeatList = new HashSet<>();
        Set<TableFieldDO> orderList = new HashSet<>();
        Set<TableFieldDO> formList = new HashSet<>();
        Set<TableFieldDO> gridList = new HashSet<>();
        Set<TableFieldDO> requiredList = new HashSet<>();
        for (TableFieldDO field : tableFields) {
            if ((!ObjectUtils.isEmpty(field.getPrimaryPk())) && (field.getPrimaryPk() == ONE)) {
                putStringIfNotEmpty(dataModel, PK_TYPE, field.getAttrType());
                putStringIfNotEmpty(dataModel, PK, field.getAttrName());
                dtoList.add(field);
                voList.add(field);
            }
            addSet(field.getFormItem(), field, dtoList, voList, formList);
            addSet(field.getGridItem(), field, voList, gridList);
            addSet(field.getQueryItem(), field, queryList);
            addSet(field.getFieldRepeat(), field, dtoList, repeatList);
            addSet(field.getGridSort(), field, orderList);
            addSet(field.getFormRequired(), field, dtoList, requiredList);
            if ((!ObjectUtils.isEmpty(field.getPrimaryPk())) && (!ObjectUtils.isEmpty(field.getGridItem()))
                    && (field.getPrimaryPk() == ZERO) && (field.getGridItem() == ZERO)
            ) {
                selectList.add(field);
            }
        }
        dataModel.put(VO_LIST, voList);
        dataModel.put(DTO_LIST, dtoList);
        dataModel.put(QUERY_LIST, queryList);
        dataModel.put(REPEAT_LIST, repeatList);
        dataModel.put(ORDER_LIST, orderList);
        dataModel.put(FORM_LIST, formList);
        dataModel.put(GRID_LIST, gridList);
        dataModel.put(REQUIRED_LIST, requiredList);
        dataModel.put(SELECT, composeSelect(selectList));
    }
    private static String composeSelect(Set<TableFieldDO> selectList) {
        if (CollectionUtils.isEmpty(selectList)) {
            return Constants.EMPTY;
        }
        StringBuilder result = new StringBuilder();
        for (TableFieldDO tableFieldDO : selectList) {
            String fieldExpression = String.format("!fieldInfo.getColumn().equals(\"%s\")", tableFieldDO.getFieldName());
            result.append(!StringUtils.hasText(result.toString()) ? fieldExpression : " && " + fieldExpression);
        }

        return "fieldInfo->" + result;
    }
    @SafeVarargs
    public static void addSet(Integer flag, TableFieldDO field, Set<TableFieldDO>... sets) {
        if (ObjectUtils.isEmpty(flag) || flag == ZERO || sets.length == ZERO) {
            return;
        }
        for (Set<TableFieldDO> set : sets) {
            set.add(field);
        }
    }

    /**
     * 用表数据来设置模版数据
     *
     * @param dataModel 模版数据
     * @param table     数据
     */
    public static void setDataModelByTable(Map<String, Object> dataModel, TableDTO table) {
        putStringIfNotEmpty(dataModel, PACKAGE, table.getPackageName());
        putStringIfNotEmpty(dataModel, PACKAGE_PATH, table.getPackageName().replace(PERIOD, File.separator));
        putStringIfNotEmpty(dataModel, VERSION, table.getVersion());
        putStringIfNotEmpty(dataModel, MODULE_NAME, table.getModuleName());
        putStringIfNotEmpty(dataModel, DB_TYPE, table.getDbType());
        putStringIfNotEmpty(dataModel, AUTHOR, table.getAuthor());
        putStringIfNotEmpty(dataModel, EMAIL, table.getEmail());
        if (ObjectUtils.isEmpty(table.getGeneratorType())) {
            table.setGeneratorType(SINGLE_MODULE);
        }
        if (table.getGeneratorType() == SINGLE_MODULE) {
            putStringIfNotEmpty(dataModel, BIZ_PATH, table.getModuleName());
            putStringIfNotEmpty(dataModel, MODEL_PATH, table.getModuleName());
            putStringIfNotEmpty(dataModel, FRONT_PATH, table.getModuleName());
        } else {
            putStringIfNotEmpty(dataModel, BIZ_PATH, table.getModuleName() + "-biz");
            putStringIfNotEmpty(dataModel, BIZ_PATH, table.getModuleName() + "-model");
            putStringIfNotEmpty(dataModel, BIZ_PATH, table.getModuleName() + "-view");
        }
        String tableName = table.getTableName();
        putStringIfNotEmpty(dataModel, TABLE_NAME, tableName);
        if (StringUtils.hasText(table.getTablePrefix())) {
            String temp = tableName.substring(table.getTablePrefix().length());
            if (StringUtils.hasText(temp) || isChar(temp.charAt(0))) {
                tableName = temp;
                if (table.getFunctionName().equals(table.getTableName())) {
                    table.setFunctionName(tableName);
                }
            }
        }
        tableName = StrUtils.underlineToCamel(tableName, false);
        putStringIfNotEmpty(dataModel, TABLE_NAME, tableName);

        putStringIfNotEmpty(dataModel, TABLE_COMMENT, changeNameFirst(table.getTableComment(), tableName));
        putStringIfNotEmpty(dataModel, CLASS_NAME, changeNameFirst(table.getClassName(), tableName));
        putStringIfNotEmpty(dataModel, FUNCTION_NAME, changeNameFirst(table.getFunctionName(), tableName));
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
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }
}

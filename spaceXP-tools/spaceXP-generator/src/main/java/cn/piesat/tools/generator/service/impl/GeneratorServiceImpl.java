package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.tools.generator.constants.Constants;
import cn.piesat.tools.generator.model.GeneratorInfo;
import cn.piesat.tools.generator.model.TemplateInfo;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.service.GeneratorService;
import cn.piesat.tools.generator.service.TableFieldService;
import cn.piesat.tools.generator.utils.ConfigUtils;
import cn.piesat.tools.generator.utils.StrUtils;
import cn.piesat.tools.generator.utils.TemplateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.clickhouse.jdbc.Main;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p/>
 * {@code @description}  :代码生成实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:50.
 *
 * @author zhouxp
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private void writeZipByTemplate(Map<String, Object> dataModel, ZipOutputStream zip) {
        GeneratorInfo generator = ConfigUtils.getGeneratorInfo();
        for (TemplateInfo template : generator.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = TemplateUtils.getContent(template.getTemplateContent(), dataModel);
            String path = TemplateUtils.getContent(template.getGeneratorPath(), dataModel);
            try {
                zip.putNextEntry(new ZipEntry(path));
                IOUtils.write(content, zip, "UTF-8");
                zip.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void writeZip(HttpServletResponse response, ByteArrayOutputStream outputStream) throws IOException {
        byte[] responseData = outputStream.toByteArray();
        // 设置响应头部
        response.reset();
        String fileName = "piesat.zip"; // 假设为模板文件夹的名称
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/octet-stream"); // 或者根据实际情况设置更准确的MIME类型
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        // 输出ZIP文件内容到响应体
        response.getOutputStream().write(responseData);
    }

    public void writeJsonToResponse(HttpServletResponse response, String data) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(data);
    }


    /**
     * 通过数据字段设置数据模版
     *
     * @param dataModel 数据模版信息
     * @param tableId   表ID
     */
    private void setDataModelByFields(Map<String, Object> dataModel, Long tableId) {
        LambdaQueryWrapper<TableFieldDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableFieldDO::getTableId, tableId);
        List<TableFieldDO> tableFieldDOS = tableFieldService.list(queryWrapper);
        dataModel.put("fieldList", tableFieldDOS);
        // 导入的包列表
        Set<String> importList = tableFieldService.getPackageByTableId(tableId);
        dataModel.put("importList", importList);
        setFieldTypeList(dataModel, tableFieldDOS);
    }

    @Resource
    private TableFieldService tableFieldService;

    /**
     * 设置字段分类信息
     */
    private void setFieldTypeList(Map<String, Object> dataModel, List<TableFieldDO> tableFieldDOS) {
        List<TableFieldDO> voList = new ArrayList<>();
        List<TableFieldDO> dtoList = new ArrayList<>();
        List<TableFieldDO> selectList = new ArrayList<>();
        List<TableFieldDO> queryList = new ArrayList<>();
        List<TableFieldDO> repeatList = new ArrayList<>();
        List<TableFieldDO> orderList = new ArrayList<>();
        for (TableFieldDO field : tableFieldDOS) {
            if (field.getPrimaryPk()==1) {
                dataModel.put("pkType",field.getAttrType());
                dataModel.put("pk",field.getAttrName());
            }
            if(field.getDto()==1){
                dtoList.add(field);
            }
            if(field.getVo()==1){
                voList.add(field);
            }
            if(field.getGridList()==0){
                selectList.add(field);
            }
            if(field.getQueryItem()==1){
                queryList.add(field);
            }
            if(field.getFieldRepeat()==1){
                repeatList.add(field);
            }
            if(field.getSortType()!=1){
                repeatList.add(field);
            }
        }
        dataModel.put("voList", voList);
        dataModel.put("dtoList", dtoList);
        dataModel.put("queryList", queryList);
        dataModel.put("repeatList", repeatList);
        dataModel.put("orderList", orderList);
        dataModel.put("select", composeSelect(selectList));
    }

    private String composeSelect(List<TableFieldDO> selectList) {
        if (CollectionUtils.isEmpty(selectList)) {
            return Constants.EMPTY;
        }
        StringBuilder result = new StringBuilder();
        for (TableFieldDO tableFieldDO : selectList) {
            String fieldExpression = String.format("fieldInfo->!fieldInfo.getColumn().equals(\"%s\")", tableFieldDO.getFieldName());
            result.append(!StringUtils.hasText(result.toString()) ? fieldExpression : " && " + fieldExpression);
        }
        return result.toString();
    }
    @Override
    public void genCode(List<TableDTO> tables, HttpServletResponse response) {
        // 代码生成器信息
        if (tables == null || tables.size() == 0) {
            try {
                writeJsonToResponse(response,OBJECT_MAPPER.writeValueAsString(ApiResult.fail("没有数据")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Map<String, Object> dataModel = new HashMap<>();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            for (TableDTO table : tables) {
                dataModel.put("openingTime", LocalDateTime.now());
                packTablesWriteZip(table, dataModel, zip);
                dataModel.clear();
            }
            writeZip(response, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void packTablesWriteZip(TableDTO table , Map<String, Object> dataModel, ZipOutputStream zip) {
            setDataModelByTable(dataModel,table);
            setDataModelByFields(dataModel,table.getId());
            writeZipByTemplate(dataModel,zip);
    }

    private void setDataModelByTable(Map<String, Object> dataModel, TableDTO table) {
        dataModel.put("package", table.getPackageName());
        dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
        dataModel.put("version", table.getVersion());
        dataModel.put("moduleName", table.getModuleName());
        dataModel.put("ModuleName", StringUtils.capitalize(table.getModuleName()));

        // 开发者信息
        dataModel.put("author", table.getAuthor());
        dataModel.put("email", table.getEmail());

        // 生成路径
        dataModel.put("bizPath", table.getModuleName()+"-biz");
        dataModel.put("apiPath", table.getModuleName()+"-api");
        dataModel.put("frontendPath", table.getModuleName()+"-view");
        String tableName = table.getTableName();
        dataModel.put("tableName", tableName);
        if (StringUtils.hasText(table.getTablePrefix())) {
            tableName = tableName.substring(table.getTablePrefix().length());
        }
        tableName = StrUtils.underlineToCamel(tableName,false);
        if (StringUtils.hasText(table.getTableComment())) {
            dataModel.put("tableComment", table.getTableComment());
        } else {
            dataModel.put("tableComment", tableName);
        }
        if (StringUtils.hasText(table.getClassName())) {
            dataModel.put("className", StringUtils.uncapitalize(table.getClassName()));
            dataModel.put("ClassName", table.getClassName());
        } else {
            dataModel.put("ClassName", StringUtils.capitalize(tableName));
            dataModel.put("className", tableName);
        }
        if (StringUtils.hasText(table.getFunctionName())) {
            dataModel.put("functionName", table.getFunctionName());
        } else {
            dataModel.put("functionName", StringUtils.uncapitalize(tableName));
        }
    }
}

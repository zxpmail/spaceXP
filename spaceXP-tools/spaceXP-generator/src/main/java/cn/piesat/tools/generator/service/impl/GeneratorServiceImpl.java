package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.tools.generator.model.GeneratorInfo;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.dto.TablesDTO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.service.GeneratorService;
import cn.piesat.tools.generator.service.TableFieldService;
import cn.piesat.tools.generator.utils.ConfigUtils;
import cn.piesat.tools.generator.utils.DateUtils;
import cn.piesat.tools.generator.utils.TemplateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private final static ObjectMapper OBJECT_MAPPER =new ObjectMapper();
    @Override
    public void generatorCode(TableDTO tableDTO, HttpServletResponse response) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableDTO);

        // 代码生成器信息
        GeneratorInfo generator = ConfigUtils.getGeneratorInfo();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(outputStream)) {

            // 渲染模板并输出
            generator.getTemplates().forEach(template -> {
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

            });
            writeZip(response, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void generatorCode(TablesDTO tablesDTO, HttpServletResponse response) {
        // 代码生成器信息
        GeneratorInfo generator = ConfigUtils.getGeneratorInfo();
        if(tablesDTO == null||tablesDTO.getTables()==null||tablesDTO.getTables().size()==0){
            writeJsonToResponse(response,OBJECT_MAPPER.writeValueAsString(ApiResult.fail("没有数据")));

        }
        ApiResult<String> test = ApiResult.fail("没有数据");
        String s = OBJECT_MAPPER.writeValueAsString(test);
        writeJsonToResponse(response,s);

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

    @Resource
    private TableFieldService tableFieldService;
    /**
     * 获取渲染的数据模型
     *
     * @param tableDTO 表ID
     */
    private Map<String, Object> getDataModel(TableDTO tableDTO) {

        LambdaQueryWrapper< TableFieldDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableFieldDO::getTableId, tableDTO.getId()) ;
        // 表信息
        List<TableFieldDO> tableFieldDOS = tableFieldService.list(queryWrapper);

        // 数据模型
        Map<String, Object> dataModel = new HashMap<>();
        setFieldTypeList(dataModel,tableFieldDOS);
        // 项目信息
        dataModel.put("package", tableDTO.getProject().getGroupId());
        dataModel.put("packagePath", tableDTO.getProject().getGroupId().replace(".", File.separator));
        dataModel.put("version", tableDTO.getProject().getVersion());
        dataModel.put("moduleName", tableDTO.getProject().getArtifactId());
        dataModel.put("ModuleName", StringUtils.capitalize(tableDTO.getProject().getArtifactId()));



        // 开发者信息
        dataModel.put("author", tableDTO.getProject().getAuthor());
        dataModel.put("email", tableDTO.getProject().getEmail());
        dataModel.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        dataModel.put("date", DateUtils.format(new Date(), DateUtils.DATE_PATTERN));

        // 导入的包列表
        Set<String> importList = tableFieldService.getPackageByTableId(tableDTO.getId());
        dataModel.put("importList", importList);

        // 表信息
        dataModel.put("tableName", tableDTO.getTableName());
        dataModel.put("tableComment", tableDTO.getTableComment());
        dataModel.put("className", StringUtils.uncapitalize(tableDTO.getClassName()));
        dataModel.put("functionName", tableDTO.getFunctionName());
        dataModel.put("ClassName", tableDTO.getClassName());
        dataModel.put("fieldList", tableFieldDOS);

        // 生成路径
        dataModel.put("backendPath", tableDTO.getProject().getArtifactId());
        dataModel.put("frontendPath",tableDTO.getProject().getArtifactId());
        return dataModel;
    }
    /**
     * 设置字段分类信息
     *
     */
    private void setFieldTypeList(Map<String, Object> dataModel, List<TableFieldDO> tableFieldDOS ) {
        // 主键列表 (支持多主键)
        List<TableFieldDO> primaryList = new ArrayList<>();
        // 查询列表
        List<TableFieldDO> queryList = new ArrayList<>();
        for (TableFieldDO field : tableFieldDOS) {
            if (field.isPrimaryPk()) {
                primaryList.add(field);
            }

        }
        dataModel.put("primaryList", primaryList);
        dataModel.put("queryList", queryList);
    }
}

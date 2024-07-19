package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.tools.generator.config.TemplatesConfig;
import cn.piesat.tools.generator.constants.Constants;
import cn.piesat.tools.generator.model.dto.BatchTableDTO;
import cn.piesat.tools.generator.model.dto.ProjectDTO;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.entity.TemplateEntity;
import cn.piesat.tools.generator.service.DataSourceService;
import cn.piesat.tools.generator.service.GeneratorService;
import cn.piesat.tools.generator.service.TableFieldService;
import cn.piesat.tools.generator.service.TableService;
import cn.piesat.tools.generator.utils.DateUtils;
import cn.piesat.tools.generator.utils.TemplateDataUtils;
import cn.piesat.tools.generator.utils.TemplateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.piesat.tools.generator.constants.Constants.*;

/**
 * <p/>
 * {@code @description}  :代码生成实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:50.
 *
 * @author zhouxp
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final TableFieldService tableFieldService;
    private final TableService tableService;
    private final DataSourceService dataSourceService;
    private final TemplatesConfig templatesConfig;

    private void writeZipByTemplate(Map<String, Object> dataModel, ZipOutputStream zip, Integer isOnly) {
        for (TemplateEntity template : templatesConfig.getTemplates()) {
            if (template.getOnly().equals(isOnly)) {
                dataModel.put(TEMPLATE_NAME, template.getName());
                String content = TemplateUtils.getContent(template.getContent(), dataModel);
                String path = TemplateUtils.getContent(template.getPath(), dataModel);
                try {
                    zip.putNextEntry(new ZipEntry(path));
                    IOUtils.write(content, zip, StandardCharsets.UTF_8.name());
                    zip.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        TemplateDataUtils.setDataModelByField(dataModel, tableFieldDOS);
    }

    @Override
    public void genTableCode(TableDTO table, HttpServletResponse response) {
        // 代码生成器信息
        if (ObjectUtils.isEmpty(table)) {
            noData(response);
            return;
        }
        genCode(table, response);
    }

    private <T> void genCode(T table, HttpServletResponse response) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            Map<String, Object> dataModel = new HashMap<>();
            if (table instanceof List) {
                @SuppressWarnings("unchecked")
                List<TableDTO> list = (List<TableDTO>) table;
                for (TableDTO item : list) {
                    writeTable(item, dataModel, zip);
                }
            } else {
                writeTable((TableDTO) table, dataModel, zip);
            }
            zip.finish();
            writeZip(response, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成单表并打包成zip
     *
     * @param table     表名
     * @param dataModel 数据模式
     * @param zip       上传zip流
     */
    private void writeTable(TableDTO table, Map<String, Object> dataModel, ZipOutputStream zip) {
        dataModel.put(Constants.CREATE_TIME, DateUtils.LocalDateTime2String(LocalDateTime.now()));
        packTablesWriteZip(table, dataModel, zip);
        dataModel.clear();
    }

    private void noData(HttpServletResponse response) {
        try {
            writeJsonToResponse(response, OBJECT_MAPPER.writeValueAsString(ApiResult.fail("没有数据")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void genProjectCode(ProjectDTO projectDTO, HttpServletResponse response) {
        if (projectDTO == null || projectDTO.getTables() == null || projectDTO.getTables().size() == 0) {
            noData(response);
            return;
        }
        saveTableDTO(projectDTO);
        Map<String, Object> dataModel = new HashMap<>();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            //writeTable(projectDTO.getTables(), dataModel, zip);
            writeProject(projectDTO, dataModel, zip);
            zip.finish();
            writeZip(response, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void genBatchTableCode(BatchTableDTO batchTableDTO, HttpServletResponse response) {
        if (ObjectUtils.isEmpty(batchTableDTO) || CollectionUtils.isEmpty(batchTableDTO.getTables()) || ObjectUtils.isEmpty(batchTableDTO.getProject())) {
            noData(response);
            return;
        }
        batchTableDTO.getTables().forEach(t -> {
            t.setAuthor(batchTableDTO.getProject().getAuthor());
            t.setVersion(batchTableDTO.getProject().getVersion());
            t.setEmail(batchTableDTO.getProject().getEmail());
            t.setPackageName(batchTableDTO.getProject().getArtifactId());
            t.setModuleName(batchTableDTO.getProject().getArtifactId());
            t.setTablePrefix(batchTableDTO.getTablePrefix());
            t.setFormLayout(batchTableDTO.getFormLayout());
            t.setProjectId(batchTableDTO.getProject().getId());
        });
        genCode(batchTableDTO.getTables(), response);
    }

    private void writeProject(ProjectDTO projectDTO, Map<String, Object> dataModel, ZipOutputStream zip) {
        dataModel.put(Constants.CREATE_TIME, DateUtils.LocalDateTime2String(LocalDateTime.now()));
        packProjectWriteZip(projectDTO, dataModel, zip);
    }


    private void packProjectWriteZip(ProjectDTO projectDTO, Map<String, Object> dataModel, ZipOutputStream zip) {
        TemplateDataUtils.setDataModelByProject(dataModel, projectDTO);
        setDataSourceInfo(dataModel, projectDTO.getTables().get(0));
        writeZipByTemplate(dataModel, zip, 1);
    }

    private void setDataSourceInfo(Map<String, Object> dataModel, TableDTO tableDTO) {
        DataSourceDO dataSourceDO = dataSourceService.getDataSourceDOByConnName(tableDTO.getConnName());
        TemplateDataUtils.setDataModelByDataSource(dataModel,dataSourceDO);
    }

    private void saveTableDTO(ProjectDTO projectDTO) {
        for (TableDTO table : projectDTO.getTables()) {
            table.setProjectId(projectDTO.getId());
            table.setAuthor(projectDTO.getAuthor());
            table.setEmail(projectDTO.getEmail());
            table.setModuleName(projectDTO.getArtifactId());
            table.setPackageName(projectDTO.getGroupId());
            table.setVersion(projectDTO.getVersion());
            if (StringUtils.hasText(projectDTO.getTablePrefix())) {
                table.setTablePrefix(projectDTO.getTablePrefix());
            }
        }
        tableService.batchUpdate(projectDTO.getTables());
    }

    private void packTablesWriteZip(TableDTO table, Map<String, Object> dataModel, ZipOutputStream zip) {
        TemplateDataUtils.setDataModelByTable(dataModel, table);
        setDataModelByFields(dataModel, table.getId());
        writeZipByTemplate(dataModel, zip, 0);
    }

}

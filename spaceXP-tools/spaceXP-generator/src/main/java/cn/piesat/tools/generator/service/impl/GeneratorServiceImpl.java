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
import lombok.extern.slf4j.Slf4j;
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
import java.util.function.BiConsumer;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final TableFieldService tableFieldService;
    private final TableService tableService;
    private final DataSourceService dataSourceService;
    private final TemplatesConfig templatesConfig;

    /**
     * 从模版中获取数据流，根据模版数据进行替换后，生成zip数据
     *
     * @param dataModel  模版数据
     * @param zip        包数据
     * @param must       1 必须生成 2当选择项目时生成，没有选项目时不生成
     * @param moduleType 生成文件的模块类型 1为单模块 2 为多模块  单模块只生成单模块文件，多模块方式生成不仅是多模块还包含单模块
     */
    private void writeZipByTemplate(Map<String, Object> dataModel, ZipOutputStream zip, Integer must, Integer moduleType) {
        for (TemplateEntity template : templatesConfig.getTemplates()) {
            if ((must.equals(template.getMust()) ) && (moduleType >= template.getModuleType())) {
                dataModel.put(TEMPLATE_NAME, template.getName());
                String content = TemplateUtils.getContent(template.getContent(), dataModel);
                String path = TemplateUtils.getContent(template.getPath(), dataModel);
                try {
                    zip.putNextEntry(new ZipEntry(path));
                    IOUtils.write(content, zip, StandardCharsets.UTF_8.name());
                    zip.closeEntry();
                } catch (IOException e) {
                    log.error("name:{},path:{}", template.getName(), template.getPath(), e);
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
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
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
    public void genTableCode(List<TableDTO> table, HttpServletResponse response) {
        // 代码生成器信息
        if (ObjectUtils.isEmpty(table)) {
            noData(response);
            return;
        }
        genCode(table, response);
    }

    private void genCode(List<TableDTO> list, HttpServletResponse response) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            writeTables(list, zip);
            zip.finish();
            writeZip(response, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成单表并打包成zip
     *
     * @param tables 表信息
     * @param zip    上传zip流
     */
    private void writeTables(List<TableDTO> tables, ZipOutputStream zip) {
        for (TableDTO item : tables) {
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put(Constants.CREATE_TIME, DateUtils.LocalDateTime2String(LocalDateTime.now()));
            packTablesWriteZip(item, dataModel, zip);
            dataModel.clear();
        }
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
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            initTables(projectDTO.getTables(),projectDTO,1);
            writeTables(projectDTO.getTables(), zip);
            writeProject(projectDTO, zip);
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
        initTables(batchTableDTO.getTables(),batchTableDTO.getProject(),batchTableDTO.getFormLayout());
        genCode(batchTableDTO.getTables(), response);
    }

    private static void initTables(List<TableDTO> tables,ProjectDTO projectDTO,Integer formLayout ) {
        tables.forEach(t -> {
            t.setAuthor(projectDTO.getAuthor());
            t.setVersion(projectDTO.getVersion());
            t.setEmail(projectDTO.getEmail());
            t.setGeneratorType(projectDTO.getType());
            t.setPackageName(projectDTO.getGroupId());
            t.setModuleName(projectDTO.getArtifactId());
            t.setTablePrefix(projectDTO.getTablePrefix());
            t.setFormLayout(formLayout);
            t.setProjectId(projectDTO.getId());
        });
    }

    private void writeProject(ProjectDTO projectDTO, ZipOutputStream zip) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(Constants.CREATE_TIME, DateUtils.LocalDateTime2String(LocalDateTime.now()));
        packProjectWriteZip(projectDTO, dataModel, zip);
    }


    private void packProjectWriteZip(ProjectDTO projectDTO, Map<String, Object> dataModel, ZipOutputStream zip) {
        TemplateDataUtils.setDataModelByProject(dataModel, projectDTO);
        setDataSourceInfo(dataModel, projectDTO.getTables().get(0));
        writeZipByTemplate(dataModel, zip, 2, projectDTO.getType());
    }

    private void setDataSourceInfo(Map<String, Object> dataModel, TableDTO tableDTO) {
        DataSourceDO dataSourceDO = dataSourceService.getDataSourceDOByConnName(tableDTO.getConnName());
        TemplateDataUtils.setDataModelByDataSource(dataModel, dataSourceDO);
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
        writeZipByTemplate(dataModel, zip, 1, 1);
    }

}

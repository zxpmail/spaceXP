package cn.piesat.tools.generator.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.piesat.tools.generator.model.GeneratorInfo;
import cn.piesat.tools.generator.model.TemplateInfo;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.service.GeneratorService;
import cn.piesat.tools.generator.service.TableFieldService;
import cn.piesat.tools.generator.utils.ConfigUtils;
import cn.piesat.tools.generator.utils.DateUtils;
import cn.piesat.tools.generator.utils.TemplateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
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
    @Override
    public byte[] generatorCode(TableDTO tableDTO) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableDTO);

        // 代码生成器信息
        GeneratorInfo generator = ConfigUtils.getGeneratorInfo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 渲染模板并输出
        for (TemplateInfo template : generator.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = TemplateUtils.getContent(template.getTemplateContent(), dataModel);
            String path = TemplateUtils.getContent(template.getGeneratorPath(), dataModel);

            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(path));
                IoUtil.writeUtf8(zip, false, content);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("模板写入失败：" + path, e);
            }
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
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
        dataModel.put("version", tableDTO.getProject().getVersion());
        dataModel.put("moduleName", tableDTO.getProject().getArtifactId());
        dataModel.put("ModuleName", StrUtil.upperFirst(tableDTO.getProject().getArtifactId()));



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
        dataModel.put("className", StrUtil.lowerFirst(tableDTO.getClassName()));
        dataModel.put("ClassName", tableDTO.getClassName());
        dataModel.put("fieldList", tableFieldDOS);


        return dataModel;
    }
    /**
     * 设置字段分类信息
     *
     */
    private void setFieldTypeList(Map<String, Object> dataModel, List<TableFieldDO> tableFieldDOS ) {
        // 主键列表 (支持多主键)
        List<TableFieldDO> primaryList = new ArrayList<>();
        for (TableFieldDO field : tableFieldDOS) {
            if (field.isPrimaryPk()) {
                primaryList.add(field);
            }
        }
        dataModel.put("primaryList", primaryList);
    }
}

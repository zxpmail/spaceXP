package cn.piesat.tools.generator.utils;


import cn.piesat.framework.dynamic.datasource.datasource.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import cn.piesat.tools.generator.config.TemplatesConfig;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.TemplateEntity;
import cn.piesat.tools.generator.service.DataSourceService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cn.piesat.tools.generator.constants.Constants.TEMPLATE_DIRECTORY;

/**
 * <p/>
 * {@code @description}  :  启动时把数据库中的数据源加入内存中
 * <p/>
 * <b>@create:</b> 2023/12/9 19:58.
 *
 * @author zhouxp
 */
@Component
@Slf4j
public class DataSourceRunner implements ApplicationRunner {
    @Resource
    private DynamicDataSource dynamicDataSource;
    @Resource
    private DataSourceService dataSourceService;

    @Resource
    private TemplatesConfig templatesConfig;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        templatesConfig.getTemplates().forEach(this::LoadContent);
        List<DataSourceDO> list = dataSourceService.list();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DataSourceDO dataSourceDO : list) {
                DataSourceProperty dataSourceProperty = new DataSourceProperty();
                BeanUtils.copyProperties(dataSourceDO,dataSourceProperty);
                dynamicDataSource.add(dataSourceProperty,dataSourceDO.getConnName());
            }
        }
    }
    private void LoadContent(TemplateEntity templateEntity) {
        try (InputStream isTemplate = this.getClass().getResourceAsStream(TEMPLATE_DIRECTORY + templateEntity.getName())) {
            templateEntity.setContent(StreamUtils.copyToString(isTemplate, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("读取模板文件: { name } 失败,cause: {}", templateEntity.getName(), e);
            System.exit(0);
        }
    }
}


package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.mapper.ProjectMapper;
import cn.piesat.tools.generator.model.entity.ProjectDO;
import cn.piesat.tools.generator.model.query.FieldTypeQuery;
import cn.piesat.tools.generator.model.vo.FieldTypeVO;
import cn.piesat.tools.generator.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :表接口实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectDO> implements ProjectService {
    @Override
    public PageResult list(PageBean pageBean, FieldTypeQuery fieldTypeQuery) {
        return null;
    }

    @Override
    public FieldTypeVO info(Long id) {
        return null;
    }

    @Override
    public Boolean add(FieldTypeVO fieldTypeVO) {
        return null;
    }

    @Override
    public Boolean update(FieldTypeVO fieldTypeVO) {
        return null;
    }

    @Override
    public Boolean delete(List<Long> ids) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
}

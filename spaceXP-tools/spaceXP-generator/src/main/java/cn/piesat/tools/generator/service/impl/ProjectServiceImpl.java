package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.mapper.ProjectMapper;
import cn.piesat.tools.generator.model.dto.ProjectDTO;
import cn.piesat.tools.generator.model.entity.ProjectDO;
import cn.piesat.tools.generator.model.query.ProjectQuery;
import cn.piesat.tools.generator.model.vo.ProjectVO;
import cn.piesat.tools.generator.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    public PageResult list(PageBean pageBean, ProjectQuery projectQuery) {
        IPage<ProjectDO> page = this.page(
                QueryUtils.getPage(pageBean),
                getWrapper(projectQuery)
        );
        return new PageResult(page.getTotal(), CopyBeanUtils.copy(page.getRecords(), ProjectVO::new));
    }

    private LambdaQueryWrapper<ProjectDO> getWrapper(ProjectQuery projectQuery) {
        LambdaQueryWrapper<ProjectDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.hasText(projectQuery.getArtifactId()),ProjectDO::getArtifactId,projectQuery.getArtifactId())
                .like(StringUtils.hasText(projectQuery.getAuthor()),ProjectDO::getAuthor,projectQuery.getAuthor())
                .like(StringUtils.hasText(projectQuery.getVersion()),ProjectDO::getVersion,projectQuery.getVersion());
        return wrapper;
    }
    @Override
    public ProjectVO info(Long id) {
        return CopyBeanUtils.copy(getById(id),ProjectVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(ProjectDTO projectDTO) {
        repeat(projectDTO);
        return save(CopyBeanUtils.copy(projectDTO,ProjectDO::new));
    }

    private void repeat(ProjectDTO projectDTO){
        LambdaQueryWrapper<ProjectDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProjectDO::getArtifactId,projectDTO.getArtifactId())
                .eq(ProjectDO::getAuthor,projectDTO.getAuthor())
                .eq(ProjectDO::getVersion,projectDTO.getVersion());
        if (count(wrapper)>0){
            throw new BaseException(CommonResponseEnum.RECORD_REPEAT);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ProjectDTO projectDTO) {
        ProjectDO byId = getById(projectDTO.getId());
        if(ObjectUtils.isEmpty(byId)){
            return false;
        }
        BeanUtils.copyProperties(projectDTO,byId,CopyBeanUtils.getNullPropertyNames(projectDTO));
        return updateById(byId);
    }


    @Override
    public Boolean delete(List<Long> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public List<ProjectVO> all() {
        return CopyBeanUtils.copy(list(),ProjectVO::new);
    }

    @Override
    public ProjectDO getDefaultProject() {
        LambdaQueryWrapper<ProjectDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectDO::getIsDefault,1);
        List<ProjectDO> list = list(wrapper);
        if(CollectionUtils.isEmpty(list)){
            ProjectDO projectDO = new ProjectDO();
            projectDO.setAuthor("zhouxp");
            projectDO.setPort(8080);
            projectDO.setEmail("zhouxiaoping@piesat.cn");
            projectDO.setDescription("test");
            projectDO.setGroupId("cn.piesat");
            projectDO.setArtifactId("test");
            projectDO.setVersion("1.0.0");
            projectDO.setIsDefault(1);
            projectDO.setType(1);
            return projectDO;
        }
        return list.get(0);
    }
}

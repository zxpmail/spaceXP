package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.mapper.ProjectMapper;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.ProjectDO;
import cn.piesat.tools.generator.model.query.FieldTypeQuery;
import cn.piesat.tools.generator.model.query.ProjectQuery;
import cn.piesat.tools.generator.model.vo.FieldTypeVO;
import cn.piesat.tools.generator.model.vo.ProjectVO;
import cn.piesat.tools.generator.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
    public Boolean add(ProjectVO projectVO) {
        repeat(projectVO);
        return save(CopyBeanUtils.copy(projectVO,ProjectDO::new));
    }

    private void repeat(ProjectVO projectVO){
        LambdaQueryWrapper<ProjectDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProjectDO::getArtifactId,projectVO.getArtifactId())
                .eq(ProjectDO::getAuthor,projectVO.getAuthor())
                .eq(ProjectDO::getVersion,projectVO.getVersion());
        if (count(wrapper)>0){
            throw new BaseException(CommonResponseEnum.RECORD_REPEAT);
        }
    }

    @Override
    public Boolean update(ProjectVO projectVO) {
        ProjectDO byId = getById(projectVO.getId());
        if(ObjectUtils.isEmpty(byId)){
            return false;
        }
        BeanUtils.copyProperties(projectVO,byId,CopyBeanUtils.getNullPropertyNames(projectVO));
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
}

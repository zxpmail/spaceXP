package cn.piesat.tools.generator.service;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.dto.FieldTypeDTO;
import cn.piesat.tools.generator.model.entity.ProjectDO;
import cn.piesat.tools.generator.model.query.FieldTypeQuery;
import cn.piesat.tools.generator.model.vo.FieldTypeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :项目服务接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:29.
 *
 * @author zhouxp
 */
public interface ProjectService extends IService<ProjectDO> {
    PageResult list(PageBean pageBean, FieldTypeQuery fieldTypeQuery);

    FieldTypeVO info(Long id);

    Boolean add(FieldTypeDTO fieldTypeDTO);

    Boolean update(FieldTypeDTO fieldTypeDTO);

    Boolean delete(List<Long> ids);

    Boolean delete(Long id);
}

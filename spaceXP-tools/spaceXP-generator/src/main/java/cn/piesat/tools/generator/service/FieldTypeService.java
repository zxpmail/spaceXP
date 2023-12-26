package cn.piesat.tools.generator.service;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.query.FieldTypeQuery;
import cn.piesat.tools.generator.model.vo.FieldTypeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :数据字段类型服务接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:29.
 *
 * @author zhouxp
 */
public interface FieldTypeService extends IService<FieldTypeDO> {
    PageResult list(PageBean pageBean, FieldTypeQuery fieldTypeQuery);

    FieldTypeVO info(Long id);

    Boolean add(FieldTypeVO fieldTypeVO);

    Boolean update(FieldTypeVO fieldTypeVO);

    Boolean delete(List<Long> ids);

    Boolean delete(Long id);

    Boolean updateIsList(Long id);

    Map<String, FieldTypeDO> getMap();
}

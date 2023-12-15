package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.mapper.FieldTypeMapper;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.query.FieldTypeQuery;
import cn.piesat.tools.generator.model.vo.FieldTypeVO;
import cn.piesat.tools.generator.service.FieldTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
public class FieldTypeServiceImpl extends ServiceImpl<FieldTypeMapper, FieldTypeDO> implements FieldTypeService {
    @Override
    public PageResult list(PageBean pageBean, FieldTypeQuery fieldTypeQuery) {
        IPage<FieldTypeDO> page = this.page(
                QueryUtils.getPage(pageBean),
                getWrapper(fieldTypeQuery)
        );
        if(CollectionUtils.isEmpty(page.getRecords())){
            return new PageResult(page.getTotal(), new ArrayList<>());
        }
        return new PageResult(page.getTotal(), CopyBeanUtils.copy(page.getRecords(),FieldTypeVO::new));

    }

    private LambdaQueryWrapper<FieldTypeDO> getWrapper(FieldTypeQuery fieldTypeQuery) {
        LambdaQueryWrapper<FieldTypeDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.hasText(fieldTypeQuery.getAttrType()),FieldTypeDO::getAttrType,fieldTypeQuery.getAttrType())
                .like(StringUtils.hasText(fieldTypeQuery.getColumnType()),FieldTypeDO::getColumnType,fieldTypeQuery.getColumnType());
        return wrapper;
    }

    @Override
    public FieldTypeVO info(Long id) {
        FieldTypeDO byId = getById(id);
        if(ObjectUtils.isEmpty(byId)){
            return null;
        }
        return CopyBeanUtils.copy(byId,FieldTypeVO::new);
    }

    @Override
    public Boolean add(FieldTypeVO fieldTypeVO) {
        repeat(fieldTypeVO);
        return save(CopyBeanUtils.copy(fieldTypeVO,FieldTypeDO::new));
    }

    private void repeat(FieldTypeVO fieldTypeVO){
        LambdaQueryWrapper<FieldTypeDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FieldTypeDO::getColumnType,fieldTypeVO.getColumnType());
        if (count(wrapper)>0){
            throw new BaseException(CommonResponseEnum.RECORD_REPEAT);
        }

    }
    @Override
    public Boolean update(FieldTypeVO fieldTypeVO) {
        FieldTypeDO byId = getById(fieldTypeVO.getId());
        BeanUtils.copyProperties(fieldTypeVO,byId,CopyBeanUtils.getNullPropertyNames(fieldTypeVO));
        return save(byId);
    }

    @Override
    public Boolean delete(List<Long> ids) {

        return removeBatchByIds(ids);
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }
}

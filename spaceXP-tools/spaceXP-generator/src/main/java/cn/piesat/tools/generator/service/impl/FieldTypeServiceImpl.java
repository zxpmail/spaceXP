package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.constants.Constants;
import cn.piesat.tools.generator.mapper.FieldTypeMapper;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.query.FieldTypeQuery;
import cn.piesat.tools.generator.model.vo.FieldTypeVO;
import cn.piesat.tools.generator.service.FieldTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Functions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return CopyBeanUtils.copy(byId,FieldTypeVO::new);
    }

    @Override
    public Boolean add(FieldTypeVO fieldTypeVO) {
        repeat(fieldTypeVO);
        FieldTypeDO copy = CopyBeanUtils.copy(fieldTypeVO, FieldTypeDO::new);
        assert copy != null;
        copy.setDeleted(Constants.DEFAULT_DELETE);
        return save(copy);
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
        if(ObjectUtils.isEmpty(byId)){
            return false;
        }
        BeanUtils.copyProperties(fieldTypeVO,byId,CopyBeanUtils.getNullPropertyNames(fieldTypeVO));
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
    public Map<String, FieldTypeDO> getMap() {
        List<FieldTypeDO> list = this.list();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().collect(Collectors.toMap(t -> t.getColumnType().toLowerCase(), Functions.identity(), (d1, d2) -> d1));
    }

    @Override
    public List<String> getFieldType() {
        return list().stream().map(FieldTypeDO::getAttrType).distinct().collect(Collectors.toList());
    }
}

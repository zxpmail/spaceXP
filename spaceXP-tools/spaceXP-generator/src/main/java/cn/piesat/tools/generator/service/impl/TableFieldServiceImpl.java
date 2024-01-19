package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.utils.CopyBeanUtils;

import cn.piesat.tools.generator.mapper.TableFieldMapper;
import cn.piesat.tools.generator.model.dto.TableFieldDTO;

import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.vo.TableFieldVO;
import cn.piesat.tools.generator.service.TableFieldService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;


import java.util.List;

import java.util.Set;

/**
 * <p/>
 * {@code @description}  :表字段接口实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
public class TableFieldServiceImpl extends ServiceImpl<TableFieldMapper, TableFieldDO> implements TableFieldService {

    @Override
    public Boolean deleteByTableId(Long tableId) {
        LambdaQueryWrapper<TableFieldDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableFieldDO::getTableId,tableId);
        return remove(wrapper);
    }

    @Override
    public Boolean deleteByTableId(List<Long> tableId) {
        LambdaQueryWrapper<TableFieldDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TableFieldDO::getTableId,tableId);
        return remove(wrapper);
    }

    /**
     * 根据表ID获取对应的包集合
     *
     * @param id 表ID
     * @return 包集合
     */
    @Override
    public Set<String> getPackageByTableId(Long id) {
        return this.baseMapper.getPackageByTableId(id);
    }

    @Override
    public List<TableFieldVO> getTableFieldsByTableId(Long id) {
        LambdaQueryWrapper<TableFieldDO> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(TableFieldDO::getTableId,id);
        List<TableFieldDO> list = list(wrapper);
        return CopyBeanUtils.copy(list,TableFieldVO::new);
    }

    @Override
    public Boolean update(List<TableFieldDTO> tableFields) {
        if(CollectionUtils.isEmpty(tableFields)){
            return false;
        }
        return updateBatchById(CopyBeanUtils.copy(tableFields,TableFieldDO::new));
    }

}

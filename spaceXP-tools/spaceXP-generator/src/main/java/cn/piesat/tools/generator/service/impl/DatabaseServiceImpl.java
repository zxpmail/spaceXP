package cn.piesat.tools.generator.service.impl;


import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.tools.generator.mapper.DatabaseMapper;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.vo.DatabaseVO;
import cn.piesat.tools.generator.service.DatabaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :数据源实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
public class DatabaseServiceImpl extends ServiceImpl<DatabaseMapper, DatabaseDO> implements DatabaseService {


    @Override
    public List<DatabaseVO> all() {
        List<DatabaseDO> list = list(Wrappers.<DatabaseDO>lambdaQuery().select(DatabaseDO::getId, DatabaseDO::getDbType, DatabaseDO::getDriverClassName, DatabaseDO::getUrl));
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return CopyBeanUtils.copy(list,DatabaseVO::new);
    }

    @Override
    public Boolean add(DatabaseVO databaseVO) {
        repeat(databaseVO);
        return save(CopyBeanUtils.copy(databaseVO,DatabaseDO::new));
    }

    private void repeat(DatabaseVO databaseVO){
        LambdaQueryWrapper<DatabaseDO> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(DatabaseDO::getDbType,databaseVO.getDbType());
        if (count(wrapper)>0){
            throw  new BaseException(CommonResponseEnum.RECORD_REPEAT);
        }
    }
    @Override
    public Boolean update(DatabaseVO databaseVO) {
        DatabaseDO byId = getById(databaseVO.getId());
        BeanUtils.copyProperties(databaseVO,byId,CopyBeanUtils.getNullPropertyNames(databaseVO));
        return updateById(byId);
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public DatabaseVO  info(Long id) {
        DatabaseDO byId = getById(id);
        if (Objects.isNull(byId)){
            return null;
        }
        return CopyBeanUtils.copy(byId,DatabaseVO::new);
    }
}

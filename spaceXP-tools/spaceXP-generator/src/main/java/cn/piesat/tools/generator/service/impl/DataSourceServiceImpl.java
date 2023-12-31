package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.mapper.DataSourceMapper;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.query.DataSourceQuery;
import cn.piesat.tools.generator.model.vo.DataSourceVO;
import cn.piesat.tools.generator.service.DataSourceService;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.service.TableService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.util.ArrayList;
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
@Slf4j
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSourceDO> implements DataSourceService {

    /**
     * 分页查询
     * @param pageBean 分页实体类
     * @param dataSourceQuery 数据源参数
     * @return 查询结果
     */
    @Override
    public PageResult list(PageBean pageBean, DataSourceQuery dataSourceQuery) {
        IPage<DataSourceDO> page = this.page(
                QueryUtils.getPage(pageBean),
                getWrapper(dataSourceQuery)
        );
        if(CollectionUtils.isEmpty(page.getRecords())){
            return new PageResult(page.getTotal(), new ArrayList<>());
        }
        return new PageResult(page.getTotal(), CopyBeanUtils.copy(page.getRecords(),DataSourceVO::new));
    }

    /**
     * 根据id查询数据源
     * @param id 数据源ID
     * @return 数据源实体
     */
    @Override
    public DataSourceVO info(Long id) {
        DataSourceDO dataSourceDO = this.getById(id);
        if(Objects.isNull(dataSourceDO)){
            return null;
        }
        return CopyBeanUtils.copy(dataSourceDO,DataSourceVO::new);
    }

    /**
     * 新增数据源
     * @param dataSourceVO 数据源DTO
     * @return 成功true 失败false
     */
    @Override
    public Boolean add(DataSourceVO dataSourceVO) {
        datasourceTest(dataSourceVO);
        repeat(dataSourceVO);
        DataSourceDO copy = CopyBeanUtils.copy(dataSourceVO, DataSourceDO::new);
        return save(copy);
    }

    /**
     * 更新数据源
     * @param dataSourceVO 数据源DTO
     * @return  成功true 失败false
     */
    @Override
    public Boolean update(DataSourceVO dataSourceVO) {
        datasourceTest(dataSourceVO);
        DataSourceDO byId = getById(dataSourceVO.getId());
        BeanUtils.copyProperties(dataSourceVO,byId,CopyBeanUtils.getNullPropertyNames(dataSourceVO));
        return updateById(byId);
    }

    /**
     * 根据集合中的Id删除记录
     * @param ids 数据源集合id
     * @return  成功true 失败false
     */

    @Override
    public Boolean delete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
        return true;
    }

    /**
     * 根据数据源id删除记录
     * @param id 数据源Id
     * @return  成功true 失败false
     */
    @Override
    public Boolean delete(Long id) {
        DataSourceDO byId = getById(id);
        if(ObjectUtils.isEmpty(byId)){
            return false;
        }
        dynamicDataSource.delete(byId.getConnName());
        return removeById(id);
    }


    @Resource
    private  DynamicDataSource dynamicDataSource;


    @Override
    public Boolean test(DataSourceVO dataSourceVO) {
        datasourceTest(dataSourceVO);
        return true;
    }

    private void datasourceTest(DataSourceVO dataSourceVO) {
        DataSourceEntity copy = CopyBeanUtils.copy(dataSourceVO, DataSourceEntity::new);
        if(Objects.isNull(copy)){
            return;
        }
        copy.setKey(dataSourceVO.getConnName());
        dynamicDataSource.test(copy);
    }

    @Resource
    private  DatabaseService databaseService;
    @Resource
    private  TableService tableService;
    @Override
    public List<TableDO> tableList(Long id) {
        DataSourceDO dataSourceDO = getById(id);
        DatabaseDO databaseDO = databaseService.getById(dataSourceDO.getDatabaseId());
        DSEntity dsEntity =new DSEntity();
        dsEntity.setDSName__(dataSourceDO.getConnName());
        return tableService.getSqlByTable(databaseDO, dataSourceDO,dsEntity);
    }

    /**
     * 判断是否记录重复
     * @param dataSourceVO  数据源DTO
     */
    private void repeat(DataSourceVO dataSourceVO){
        LambdaQueryWrapper<DataSourceDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataSourceDO::getConnName,dataSourceVO.getConnName());
        if (count(wrapper)>0){
            throw new BaseException(CommonResponseEnum.RECORD_REPEAT);
        }
    }

    /**
     * 拼接wrapper
     * @param dataSourceQuery 数据源查询条件
     * @return 拼接好wrapper
     */
    private LambdaQueryWrapper<DataSourceDO> getWrapper(DataSourceQuery dataSourceQuery){
        LambdaQueryWrapper<DataSourceDO> wrapper = Wrappers.lambdaQuery();
        if(!Objects.isNull(dataSourceQuery)){
            wrapper.eq(StringUtils.hasText(dataSourceQuery.getDbType()),DataSourceDO::getDbType,dataSourceQuery.getDbType())
                    .like(StringUtils.hasText(dataSourceQuery.getConnName()),DataSourceDO::getConnName,dataSourceQuery.getConnName());
        }
        return wrapper;
    }

}

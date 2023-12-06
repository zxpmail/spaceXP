package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.mapper.DataSourceMapper;
import cn.piesat.tools.generator.model.dto.DataSourceDTO;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.query.DataSourceQuery;
import cn.piesat.tools.generator.model.vo.DataSourceVO;
import cn.piesat.tools.generator.service.DataSourceService;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.utils.DbUtils;
import cn.piesat.tools.generator.utils.GenUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.sql.Connection;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :数据源实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
@AllArgsConstructor
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
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id查询数据源
     * @param id 数据源ID
     * @return 数据源实体
     */
    @Override
    public DataSourceVO info(Long id) {
        DataSourceDO dataSourceDO = this.getById(id);
        return CopyBeanUtils.copy(dataSourceDO,DataSourceVO::new);
    }

    /**
     * 新增数据源
     * @param dataSourceDTO 数据源DTO
     * @return 成功true 失败false
     */
    @Override
    public Boolean add(DataSourceDTO dataSourceDTO) {
        repeat(dataSourceDTO);
        DataSourceDO copy = CopyBeanUtils.copy(dataSourceDTO, DataSourceDO::new);
        copy.setConnUrl(genConnUrl(dataSourceDTO));
        return save(copy);
    }

    private String genConnUrl(DataSourceDTO dataSourceDTO){
        LambdaQueryWrapper<DatabaseDO> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(DatabaseDO::getDbType,dataSourceDTO.getDbType());
        DatabaseDO one = databaseService.getOne(wrapper);
        return one.getUrlPrefix() + ":" +
                dataSourceDTO.getPort() +
                one.getUrlInfix() +
                dataSourceDTO.getDatabaseName() +
                one.getUrlSuffix();
    }
    /**
     * 更新数据源
     * @param dataSourceDTO 数据源DTO
     * @return  成功true 失败false
     */
    @Override
    public Boolean update(DataSourceDTO dataSourceDTO) {
        DataSourceDO byId = getById(dataSourceDTO.getId());
        BeanUtils.copyProperties(dataSourceDTO,byId,CopyBeanUtils.getNullPropertyNames(dataSourceDTO));
        byId.setConnUrl(genConnUrl(dataSourceDTO));
        return save(byId);
    }

    /**
     * 根据集合中的Id删除记录
     * @param ids 数据源集合id
     * @return  成功true 失败false
     */

    @Override
    public Boolean delete(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return true;
        }
        return removeBatchByIds(ids);
    }

    /**
     * 根据数据源id删除记录
     * @param id 数据源Id
     * @return  成功true 失败false
     */
    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    private final DatabaseService databaseService;
    @Override
    public String test(Long id) {
        DataSourceDO byId = getById(id);
        LambdaQueryWrapper<DatabaseDO> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(DatabaseDO::getDbType,byId);
        DatabaseDO one = databaseService.getOne(wrapper);
        try {
            DbUtils.getConnection(byId.getDbType(),one.getDriver(),byId.getConnUrl(),byId.getUsername(),byId.getPassword());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("连接失败，请检查配置信息");

        }
        return "连接成功";
    }

    @Override
    public List<TableDO> tableList(Long id) {
        DataSourceDO byId = getById(id);
        LambdaQueryWrapper<DatabaseDO> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(DatabaseDO::getDbType,byId);
        DatabaseDO one = databaseService.getOne(wrapper);
        try {
            Connection connection = DbUtils.getConnection(byId.getDbType(), one.getDriver(), byId.getConnUrl(), byId.getUsername(), byId.getPassword());
            // 根据数据源，获取全部数据表
            return GenUtils.getTableList(connection,one);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("数据源配置错误，请检查数据源配置！");
        }
    }

    /**
     * 判断是否记录重复
     * @param dataSourceDTO  数据源DTO
     */
    private void repeat(DataSourceDTO dataSourceDTO){
        LambdaQueryWrapper<DataSourceDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataSourceDO::getConnName,dataSourceDTO.getConnName());
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
        wrapper.eq(StringUtils.hasText(dataSourceQuery.getDbType()),DataSourceDO::getDbType,dataSourceQuery.getDbType())
                .like(StringUtils.hasText(dataSourceQuery.getConnName()),DataSourceDO::getConnName,dataSourceQuery.getConnName());
        return wrapper;
    }

}

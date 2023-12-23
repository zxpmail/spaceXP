package cn.piesat.tools.generator.mapper;

import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.tools.generator.model.entity.TableDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :表Mapper
 * <p/>
 * <b>@create:</b> 2023/12/4 10:46.
 *
 * @author zhouxp
 */
@Mapper
public interface TableMapper extends BaseMapper<TableDO> {

    @Select("${sql}")
    List<TableDO> getSqlByTable(String sql);
}

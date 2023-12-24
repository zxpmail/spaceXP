package cn.piesat.tools.generator.mapper;

import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :表字段mapper
 * <p/>
 * <b>@create:</b> 2023/12/4 10:55.
 *
 * @author zhouxp
 */
@Mapper
public interface TableFieldMapper extends BaseMapper<TableFieldDO> {
    @Select("${sql}")
    List<TableFieldDO> getFieldBySql(String sql);
}

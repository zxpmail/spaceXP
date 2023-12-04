package cn.piesat.tools.generator.mapper;

import cn.piesat.tools.generator.model.entity.TableDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}

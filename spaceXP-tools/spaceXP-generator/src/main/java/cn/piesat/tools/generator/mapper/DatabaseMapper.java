package cn.piesat.tools.generator.mapper;

import cn.piesat.tools.generator.model.entity.DatabaseDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p/>
 * {@code @description}  :数据库信息Mapper
 * <p/>
 * <b>@create:</b> 2023/12/4 11:29.
 *
 * @author zhouxp
 */
@Mapper
public interface DatabaseMapper extends BaseMapper<DatabaseDO> {
}

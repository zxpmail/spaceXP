package cn.piesat.tools.generator.mapper;

import cn.piesat.tools.generator.model.entity.TableFieldDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

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
    @Select("select t1.package_name,table_id from gen_field_type t1,gen_table_field t2 where t1.attr_type = t2.attr_type and t1.package_name is not null and t1.package_name !='' and t2.table_id = #{tableId}")
    Set<String>  getPackageByTableId(Long tableId);
}

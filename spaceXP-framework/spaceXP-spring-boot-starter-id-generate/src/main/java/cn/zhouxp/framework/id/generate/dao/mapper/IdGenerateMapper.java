package cn.zhouxp.framework.id.generate.dao.mapper;

import cn.zhouxp.framework.id.generate.dao.po.IdGeneratePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-05 20:21:50
 *
 * @author zhouxp
 */
@Mapper
public interface IdGenerateMapper extends BaseMapper<IdGeneratePO> {

    @Update("update sys_id_generate set end = end + step,start=start + step,version=version + 1 where id =#{id} and version=#{version}")
    int updateNewIdVersion(@Param("id") Long id, @Param("version") int version);

    @Select("select * from sys_id_generate")
    List<IdGeneratePO> selectAll();
}

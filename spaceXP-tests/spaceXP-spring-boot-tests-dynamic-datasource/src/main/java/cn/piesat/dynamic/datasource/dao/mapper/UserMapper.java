package cn.piesat.dynamic.datasource.dao.mapper;



import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.framework.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/12/7 16:01.
 *
 * @author zhouxp
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    @Select("select * from user")
    @DS
    List<UserDO> queryAllWithMaster();

    @Select("select * from user")
    @DS(value = "slave")
    List<UserDO> queryAllWithSlave();
}

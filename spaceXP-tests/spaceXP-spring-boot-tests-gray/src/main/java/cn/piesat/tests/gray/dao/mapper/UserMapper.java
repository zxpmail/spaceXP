package cn.piesat.tests.gray.dao.mapper;



import cn.piesat.tests.gray.model.entity.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
@Mapper

public interface UserMapper extends BaseMapper<UserDO> {
	
}

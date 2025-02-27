package cn.piesat.tests.redis.mapper;


import cn.piesat.tests.redis.entity.UserDO;
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

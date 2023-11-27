package cn.piesat.multi.tenant.dao.mapper;



import cn.piesat.multi.tenant.model.entity.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息
 * 
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-01-16 08:52:49
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
	
}

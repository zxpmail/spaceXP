package cn.piesat.tools.log.biz.dao.mapper;


import cn.piesat.tools.log.biz.model.entity.LoginLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统访问记录
 * 
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:02
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogDO> {
	
}

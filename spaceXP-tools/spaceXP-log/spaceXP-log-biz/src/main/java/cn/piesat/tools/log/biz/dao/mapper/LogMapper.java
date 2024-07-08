package cn.piesat.tools.log.biz.dao.mapper;


import cn.piesat.tools.log.biz.model.entity.LogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行日志
 * 
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-08 15:23:01
 */
@Mapper
public interface LogMapper extends BaseMapper<LogDO> {
	
}

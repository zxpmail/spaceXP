package cn.piesat.tools.generator.service;

import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.vo.DatabaseVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p/>
 * {@code @description}  :数据源服务接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:29.
 *
 * @author zhouxp
 */
public interface DatabaseService extends IService<DatabaseDO> {


    List<DatabaseVO> all();

    Boolean add(DatabaseVO databaseVO);

    Boolean update(DatabaseVO databaseVO);

    Boolean delete(Long id);

    DatabaseVO  info(Long id);
}

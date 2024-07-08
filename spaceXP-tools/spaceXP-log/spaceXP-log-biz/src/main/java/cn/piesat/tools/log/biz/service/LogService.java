package cn.piesat.tools.log.biz.service;



import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.tools.log.api.model.query.LogQuery;
import cn.piesat.tools.log.api.model.vo.LogVO;
import cn.piesat.tools.log.biz.model.entity.LogDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 执行日志Service接口
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-04 17:13:20
 */
public interface LogService extends IService<LogDO> {

    /**
     * 分页查询
     *
     * @param pageBean {@link PageBean} 分页对象
     * @param logQuery {@link LogQuery} 执行日志查询对象
     * @return {@link PageResult} 查询结果
    */
    PageResult list(PageBean pageBean, LogQuery logQuery);

    /**
     * 根据id查询
     *
     * @param id id
    */
    LogVO info(Serializable id);



    Boolean delete(LocalDateTime startTime, LocalDateTime endTime);
}


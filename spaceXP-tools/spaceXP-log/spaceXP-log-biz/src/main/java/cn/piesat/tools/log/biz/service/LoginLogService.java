package cn.piesat.tools.log.biz.service;



import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.tools.log.api.model.query.LoginLogQuery;
import cn.piesat.tools.log.api.model.vo.LoginLogVO;
import cn.piesat.tools.log.biz.model.entity.LoginLogDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 系统访问记录Service接口
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-04 13:38:05
 */
public interface LoginLogService extends IService<LoginLogDO> {

    /**
     * 分页查询
     *
     * @param pageBean {@link PageBean} 分页对象
     * @param loginLogQuery {@link LoginLogQuery} 系统访问记录查询对象
     * @return {@link PageResult} 查询结果
    */
    PageResult list(PageBean pageBean, LoginLogQuery loginLogQuery);

    /**
     * 根据id查询
     *
     * @param id id
     * @return {@link LoginLogVO}
    */
    LoginLogVO info(Serializable id);


    /**
     * 根据id删除
     *
     * @param startTime      开始时间
     * @param endTime   结束时间
     * @return false or true
     */
    Boolean delete(LocalDateTime startTime, LocalDateTime endTime);
}


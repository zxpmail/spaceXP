package cn.piesat.tools.log.biz.service.impl;



import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;

import cn.piesat.tools.log.api.model.query.LogQuery;
import cn.piesat.tools.log.api.model.vo.LogVO;
import cn.piesat.tools.log.biz.dao.mapper.LogMapper;
import cn.piesat.tools.log.biz.exception.LogBizException;
import cn.piesat.tools.log.biz.exception.LogResponseEnum;
import cn.piesat.tools.log.biz.model.entity.LogDO;
import cn.piesat.tools.log.biz.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 执行日志Service实现类
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-04 17:13:20
 */
@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogMapper, LogDO> implements LogService {

    @Override
    public PageResult list(PageBean pageBean, LogQuery logQuery) {
        LambdaQueryWrapper<LogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(logQuery.getModule()), LogDO::getModule, logQuery.getModule());
        wrapper.eq(!ObjectUtils.isEmpty(logQuery.getType()), LogDO::getType, logQuery.getType());
        wrapper.between((!ObjectUtils.isEmpty(logQuery.getStartTime())||!ObjectUtils.isEmpty(logQuery.getEndTime())), LogDO::getCreateTime, logQuery.getStartTime(), logQuery.getEndTime());
        wrapper.orderByDesc(LogDO::getCreateTime);
        wrapper.select(LogDO.class, f -> !f.getColumn().equals("params")
                && !f.getColumn().equals("response_data")
                && !f.getColumn().equals("exception_detail")
                && !f.getColumn().equals("exception_desc"));
        IPage<LogDO> page = this.page(QueryUtils.getPage(pageBean), wrapper);
        return new PageResult(page.getTotal(), CopyBeanUtils.copy(page.getRecords(), LogVO::new));
    }

    @Override
    public LogVO info(Serializable id) {
        return CopyBeanUtils.copy(getById(id),LogVO::new);
    }

    @Override
    public Boolean delete(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new LogBizException(LogResponseEnum.LOG_START_LT_END_TIME);
        }
        LambdaQueryWrapper<LogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(LogDO::getCreateTime, startTime, endTime);
        return remove(wrapper);
    }

}



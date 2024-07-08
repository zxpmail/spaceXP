package cn.piesat.tools.log.biz.service.impl;


import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;

import cn.piesat.tools.log.api.model.query.LoginLogQuery;
import cn.piesat.tools.log.api.model.vo.LoginLogVO;
import cn.piesat.tools.log.biz.dao.mapper.LoginLogMapper;
import cn.piesat.tools.log.biz.exception.LogBizException;
import cn.piesat.tools.log.biz.exception.LogResponseEnum;
import cn.piesat.tools.log.biz.model.entity.LoginLogDO;
import cn.piesat.tools.log.biz.service.LoginLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统访问记录Service实现类
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-04 13:38:05
 */
@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLogDO> implements LoginLogService {

    @Override
    public PageResult list(PageBean pageBean, LoginLogQuery loginLogQuery) {
        LambdaQueryWrapper<LoginLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(loginLogQuery.getUserName()), LoginLogDO::getUserName, loginLogQuery.getUserName());
        wrapper.like(StringUtils.hasText(loginLogQuery.getIp()), LoginLogDO::getIp, loginLogQuery.getIp());
        wrapper.between((!ObjectUtils.isEmpty(loginLogQuery.getStartTime())||!ObjectUtils.isEmpty(loginLogQuery.getEndTime())), LoginLogDO::getCreateTime, loginLogQuery.getStartTime(), loginLogQuery.getEndTime());
        wrapper.orderByDesc(LoginLogDO::getCreateTime);
        wrapper.select(LoginLogDO.class, f -> !f.getColumn().equals("msg"));
        IPage<LoginLogDO> page = this.page(QueryUtils.getPage(pageBean), wrapper);
        return new PageResult(page.getTotal(), CopyBeanUtils.copy(page.getRecords(), LoginLogVO::new));
    }

    @Override
    public LoginLogVO info(Serializable id) {
        return CopyBeanUtils.copy(getById(id),LoginLogVO::new);
    }

    @Override
    public Boolean delete(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new LogBizException(LogResponseEnum.LOG_START_LT_END_TIME);
        }
        LambdaQueryWrapper<LoginLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(LoginLogDO::getCreateTime, startTime, endTime);
        return remove(wrapper);
    }

}

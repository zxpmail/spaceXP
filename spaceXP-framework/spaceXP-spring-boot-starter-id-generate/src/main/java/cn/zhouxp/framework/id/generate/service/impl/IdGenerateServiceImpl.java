package cn.zhouxp.framework.id.generate.service.impl;

import cn.zhouxp.framework.id.generate.dao.mapper.IdGenerateMapper;
import cn.zhouxp.framework.id.generate.service.IdGenerateService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-05 20:28:03
 *
 * @author zhouxp
 */
@Service
public class IdGenerateServiceImpl implements IdGenerateService {
    @Setter(onMethod_ = @Autowired)
    private IdGenerateMapper idGenerateMapper;

    @Override
    public Long generateId(String biz) {
        return 0L;
    }
}

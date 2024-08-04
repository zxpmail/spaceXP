package cn.piesat.tests.mybaits.plus.service.impl;

import cn.piesat.framework.mybatis.plus.core.AutoFillMetaObjectService;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}  :实现自动填充功能
 * <p/>
 * <b>@create:</b> 2024/1/26 13:47.
 *
 * @author zhouxp
 */
@Service
public class AutoFillMetaObjectServiceImpl implements AutoFillMetaObjectService {

    @Override
    public void insertFill(MetaObjectHandler metaObjectHandler, MetaObject metaObject) {
           // metaObjectHandler.strictInsertFill(metaObject, "passwordUpdateTime", LocalDateTime::now, LocalDateTime.class);

    }
    @Override
    public void updateFill(MetaObjectHandler metaObjectHandler, MetaObject metaObject) {
        if (metaObject.hasGetter("passwordUpdateTime") && metaObject.hasSetter("passwordUpdateTime")) {
            metaObject.setValue("passwordUpdateTime", null);
            metaObjectHandler.strictUpdateFill(metaObject, "passwordUpdateTime", LocalDateTime::now, LocalDateTime.class);
        }
    }
}

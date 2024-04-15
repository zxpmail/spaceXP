package cn.piesat.framework.mybatis.plus.core;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * <p/>
 * {@code @description}  :自动填充服务接口
 * 用户可能自定义填充增加或更新数据实现此接口即可
 * <p/>
 * <b>@create:</b> 2024/1/26 12:51.
 *
 * @author zhouxp
 */
public interface AutoFillMetaObjectService {
    default void insertFill(MetaObjectHandler metaObjectHandler,MetaObject metaObject) {

    }
    default void updateFill(MetaObjectHandler metaObjectHandler,MetaObject metaObject) {

    }
}

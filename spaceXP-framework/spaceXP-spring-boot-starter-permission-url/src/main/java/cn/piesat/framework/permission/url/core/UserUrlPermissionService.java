package cn.piesat.framework.permission.url.core;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :从数据库中获取token及信息接口
 * <p/>
 * <b>@create:</b> 2023/9/12 11:17.
 *
 * @author zhouxp
 */
public interface UserUrlPermissionService {
    /**
     * 获取当前用户URL
     * @return 用户URL列表
     */
    List<String> getUrl(Long userId);

    /**
     * 检测token是否合法
     * @return 如果true就进行放行，如果false就拦截
     */
    default void checkToken(){

    }

    /**
     * 对token进行续约操作就重写此方法
     */
    default  void tokenRenewed(){

    }

    /**
     *向header域中增加值
     */
    default Map<String,String> getHeaderValue(){
        return null;
    }
}

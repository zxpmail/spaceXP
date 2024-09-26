package cn.piesat.framework.sse.core;


import java.util.Map;

/**
 * <p/>
 * {@code @description}: 回调类
 * <p/>
 * {@code @create}: 2024-06-20 15:04
 * {@code @author}: zhouxp
 */
public interface CallbackService {
    /**
     * 当登录时增加用户组功能
     * @param userId 用户ID
     * @param appId 应用ID
     */
    default   void addUser2Group(String userId, String appId, Map<String, Object> attributes){
    }

    /**
     * 当session关闭时调用
     * @param userId 用户ID
     * @param appId 应用ID
     */
    default void sessionClose(String userId,String appId){}
}

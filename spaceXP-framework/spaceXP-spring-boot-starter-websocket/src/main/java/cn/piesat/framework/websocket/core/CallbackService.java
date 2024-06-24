package cn.piesat.framework.websocket.core;


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
    default   void addUser2Group(String userId,Integer appId){
    }

    /**
     * 当session关闭时调用
     * @param userId 用户ID
     * @param appId 应用ID
     */
    default void sessionClose(String userId,Integer appId){}
}

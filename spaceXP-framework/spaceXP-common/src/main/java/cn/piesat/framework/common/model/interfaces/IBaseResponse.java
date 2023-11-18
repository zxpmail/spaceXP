package cn.piesat.framework.common.model.interfaces;

/**
 * <p/>
 * {@code @description}  :通用响应接口类，项目中响应枚举类要继承此类
 * <p/>
 * <b>@create:</b> 2023/9/25 9:04.
 *
 * @author zhouxp
 */
public interface IBaseResponse {
    /**
     * 取得响应代码
     * @return 响应代码值
     */
    Integer getCode();

    /**
     * 取得响应消息
     * @return 响应消息
     */
    String getMessage();
}

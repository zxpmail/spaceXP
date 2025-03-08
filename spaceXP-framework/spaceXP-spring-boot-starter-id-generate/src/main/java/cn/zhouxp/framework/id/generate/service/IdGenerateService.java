package cn.zhouxp.framework.id.generate.service;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-05 20:26:51
 *
 * @author zhouxp
 */
public interface IdGenerateService {
    /**
     * 生成id
     *
     * @param biz 业务标识
     * @return id 生成的id
     */
    Long generateId(String biz);
}

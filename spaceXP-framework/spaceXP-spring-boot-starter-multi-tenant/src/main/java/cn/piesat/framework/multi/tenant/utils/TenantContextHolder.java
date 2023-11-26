package cn.piesat.framework.multi.tenant.utils;

import cn.piesat.framework.common.model.dto.TwoDTO;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p/>
 * {@code @description}  : 支持父子线程之间的数据传递
 * <p/>
 * <b>@create:</b> 2023/9/5 15:19.
 *
 * @author zhouxp
 */
public class TenantContextHolder {
    private static final ThreadLocal<TwoDTO<Long,String>> CONTEXT = new TransmittableThreadLocal<>();

    public static void setTenant(TwoDTO<Long,String> tenant) {
        CONTEXT.set(tenant);
    }

    public static TwoDTO<Long,String> getTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}

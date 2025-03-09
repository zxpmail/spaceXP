package cn.zhouxp.framework.id.generate.properties;

import lombok.Data;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-09 13:51:44
 *
 * @author zhouxp
 */
@Data
public class ThreadPoolProperty {
    private int corePoolSize = 8;
    private int maxPoolSize = 16;
    private int queueCapacity = 1000;
    private String namePrefix = "execution_";
    private int keepAliveSeconds = 1000;
}

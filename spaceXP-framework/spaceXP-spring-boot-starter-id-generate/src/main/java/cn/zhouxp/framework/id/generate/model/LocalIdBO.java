package cn.zhouxp.framework.id.generate.model;

import lombok.Data;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p/>
 * {@code @description}  : 本地id业务规则
 * <p/>
 * <b>@create:</b> 2025-03-09 13:20:18
 *
 * @author zhouxp
 */
@Data
public class LocalIdBO {

    /**
     * 无顺序队列中Id
     */
    private ConcurrentLinkedQueue<Long> unorderedIdQueue;
    /**
     * 当前有序id的值
     */
    private AtomicLong currentOrderId;

    /**
     * 当前id段的开始值
     */
    private Long start;
    /**
     * 当前id段的结束值
     */
    private Long end;

}

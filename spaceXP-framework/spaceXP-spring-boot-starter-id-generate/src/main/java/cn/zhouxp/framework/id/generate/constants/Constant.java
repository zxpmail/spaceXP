package cn.zhouxp.framework.id.generate.constants;

/**
 * <p/>
 * {@code @description}  : 常量类
 * <p/>
 * <b>@create:</b> 2025-03-09 13:45:32
 *
 * @author zhouxp
 */
public interface Constant {
    /**
     * 当队列中元素个数达到此值时，将进行更新
     */
    float UPDATE_RATE = 0.75f;
    /**
     * 是否有序 1 有序 0 无序
     */
    int ORDERED = 1;
}

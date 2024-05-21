package cn.piesat.tests.mybaits.plus.config;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-05-21 9:24.
 *
 * @author zhouxp
 */
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextCopyingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return () -> {
                try {
                    RequestContextHolder.setRequestAttributes(attributes);
                    runnable.run();
                } finally {
                    // 确保在任务完成后重置，避免线程上下文的污染
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        }
        return runnable; // 如果没有请求上下文，直接返回原始任务
    }
}

package cn.piesat.framework.log.core;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * <p/>
 * {@code @description}  : callable包装
 * <p/>
 * <b>@create:</b> 2023/5/11 9:13.
 *
 * @author zhouxp
 */
public class ContextAwareCallable<T> implements Callable<T> {
    private final Callable<T> task;
    private final RequestAttributes context;

    public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
        this.task = task;
        this.context = context;
    }

    @Override
    public T call() throws Exception {
        if (context != null) {
            RequestContextHolder.setRequestAttributes(context);
        }

        try {
            return task.call();
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}


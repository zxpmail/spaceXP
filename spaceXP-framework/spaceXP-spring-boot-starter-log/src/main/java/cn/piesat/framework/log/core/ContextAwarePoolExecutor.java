

package cn.piesat.framework.log.core;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * <p/>
 * {@code @description}  : threadPool包装
 * <p/>
 * <b>@create:</b> 2023/5/11 9:14.
 *
 * @author zhouxp
 */


public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
    @Override
    public <T> Future<T> submit( Callable<T> task) {
        return super.submit(new ContextAwareCallable<>(task, RequestContextHolder.currentRequestAttributes()));
    }

    @Override
    public <T> ListenableFuture<T>   submitListenable( Callable<T> task) {
        return super.submitListenable(new ContextAwareCallable<>(task, RequestContextHolder.currentRequestAttributes()));
    }
}

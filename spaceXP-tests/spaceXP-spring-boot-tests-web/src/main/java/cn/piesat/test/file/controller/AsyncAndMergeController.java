package cn.piesat.test.file.controller;

import cn.piesat.test.file.service.TestService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-17 10:58
 * {@code @author}: zhouxp
 */
@RestController
@RequestMapping("/asyncAndMerge")
public class AsyncAndMergeController {

    @Setter(onMethod_ =@Autowired)
    private TestService testService;
    /*** 异步，不阻塞Tomcat的线程 ，提升Tomcat吞吐量***/
    @RequestMapping("/async")
    public DeferredResult<String> async() {
        System.out.println(" 当前线程 外部 " + Thread.currentThread().getName());
        DeferredResult<String> result = new DeferredResult<>();
        CompletableFuture.supplyAsync(testService::testDeferredResult).whenComplete((res,ex)->result.setResult(res));
        return result;
    }

    //用法就是返回Callable，在call方法写业务逻辑
    @RequestMapping("/async2")
    public Callable<String> async2() {
        System.out.println(" 当前线程 外部 " + Thread.currentThread().getName());
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(" 当前线程 内部 " + Thread.currentThread().getName());
                Thread.sleep(10000);
                System.out.println(" 当前线程 内部 " + Thread.currentThread().getName());
                return "success";
            }
        };
        return callable;
    }
}

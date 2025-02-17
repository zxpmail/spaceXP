package cn.piesat.test.file.service;

import cn.piesat.framework.common.annotation.ServiceValidation;
import cn.piesat.test.file.model.entity.Student;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-17 10:54
 * {@code @author}: zhouxp
 */
@Service
public class TestServiceImpl implements TestService{
    @Override
    public String testDeferredResult() {
        System.out.println("内部线程 名称 "+Thread.currentThread().getName());
        return "testDeferredResult";
    }

    @ServiceValidation
    @Override
    public Student testStu(Student stu, Integer i) {
        return null;
    }
}

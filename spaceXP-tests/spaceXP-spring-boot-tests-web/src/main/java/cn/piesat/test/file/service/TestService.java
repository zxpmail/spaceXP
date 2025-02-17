package cn.piesat.test.file.service;

import cn.piesat.framework.common.annotation.ServiceValidation;
import cn.piesat.test.file.model.entity.Student;

import org.springframework.stereotype.Service;


import javax.validation.constraints.NotNull;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-08-09 11:15
 * {@code @author}: zhouxp
 */

public interface TestService {


    String testDeferredResult();

    Student testStu( @ServiceValidation Student stu ,@NotNull(message = "参数不能为空") Integer i);
}

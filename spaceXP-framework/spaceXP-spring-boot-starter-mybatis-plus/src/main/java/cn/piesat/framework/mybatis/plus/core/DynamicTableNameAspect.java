package cn.piesat.framework.mybatis.plus.core;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.mybatis.plus.model.TableNameEntity;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Pointcut;


/**
 * <p/>
 * {@code @description}  :拦截含有DynamicTableName注解AOP
 * 注意：第一个参数必须是表名
 * <p/>
 * <b>@create:</b> 2023/10/8 14:09.
 *
 * @author zhouxp
 */
@Slf4j
@Aspect
public class DynamicTableNameAspect {

    /***
     * 定义controller切入点拦截规则，拦截OpLog注解的方法
     */
    @Pointcut("@annotation(cn.piesat.framework.mybatis.plus.annotation.DynamicTableName)")
    public void logAspect() {

    }

    /***
     * 拦截控制层的操作日志
     */
    @Around(value = "logAspect()")
    public Object changeTableName(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        String tableName = "";
        boolean isSetDynamicTableName = false;
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof TableNameEntity) {
                    tableName = ((TableNameEntity) arg).getTableName();
                    DynamicTableNameHandler.setData(tableName);
                    isSetDynamicTableName =true;
                    break;
                }
            }
        }
        try {
            return point.proceed();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        } finally {
            if(isSetDynamicTableName) {
                DynamicTableNameHandler.removeData();
            }
        }
    }
}

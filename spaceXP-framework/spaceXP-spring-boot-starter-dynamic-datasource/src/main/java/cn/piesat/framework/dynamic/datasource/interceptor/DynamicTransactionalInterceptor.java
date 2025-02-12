package cn.piesat.framework.dynamic.datasource.interceptor;

import cn.piesat.framework.dynamic.datasource.transaction.TransactionGlobalIdContextHolder;
import cn.piesat.framework.dynamic.datasource.transaction.TransactionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

/**
 * <p/>
 * {@code @description}: 动态数据源事务管理拦截器
 * <p/>
 * {@code @create}: 2025-02-12 15:12
 * {@code @author}: zhouxp
 */
@Slf4j
public class DynamicTransactionalInterceptor implements MethodInterceptor {
    /**
     * 判断是否有全局事务Id，有则表示开启全局事务；否则开启全局事务。
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String txGlobalId = TransactionGlobalIdContextHolder.getTxGlobalId();
        if (StringUtils.hasText(txGlobalId)) {
            return invocation.proceed();
        }

        Object result;
        boolean committed = false;
        try {
            TransactionUtil.startTransaction();
            result = invocation.proceed();
            committed = true;
            TransactionUtil.commit();
        } catch (Exception e) {
            log.error("事务执行失败: {}", e.getMessage(), e);
            TransactionUtil.rollback();
            throw e instanceof RuntimeException ? e : new RuntimeException(e);
        } finally {
            if (!committed) {
                try {
                    TransactionUtil.rollback();
                } catch (Exception rollbackEx) {
                    log.error("回滚事务时发生错误: {}", rollbackEx.getMessage(), rollbackEx);
                }
            }
        }
        return result;
    }
}

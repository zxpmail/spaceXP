package cn.piesat.framework.dynamic.datasource.utils;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p/>
 * {@code @description}  : //此类提供线程局部变量。这些变量不同于它们的正常对应关系是每个线程访问一个线程(通过get、set方法),有自己的独立初始化变量的副本。
 * <p/>
 * <b>@create:</b> 2023/12/9 11:02.
 *
 * @author zhouxp
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> DATASOURCE_HOLDER = new TransmittableThreadLocal<>();
    /**
     * 设置数据源
     * @param dataSourceName 数据源名称
     */
    public static void setDataSource(String dataSourceName){
        DATASOURCE_HOLDER.set(dataSourceName);
    }

    /**
     * 获取当前线程的数据源
     * @return 数据源名称
     */
    public static String getDataSource(){
        return DATASOURCE_HOLDER.get();
    }

    /**
     * 删除当前数据源
     */
    public static void removeDataSource(){
        DATASOURCE_HOLDER.remove();
    }
}

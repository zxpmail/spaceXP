package cn.piesat.framework.mybatis.plus.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.util.List;

/**
 * <p/>
 * {@code @description}  : 按条件，组成动态表名
 * <p/>
 * <b>@create:</b> 2023/11/15 17:31.
 *
 * @author zhouxp
 */
public class DynamicTableNameHandler implements TableNameHandler {

    //用于记录哪些表可以使用动态表名处理器（即哪些表分表）
    private final List<String> tableNames;


    //构造函数，构造动态表名处理器的时候，传递tableNames参数
    public DynamicTableNameHandler(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    //每个请求线程维护一个数据，避免多线程数据冲突。所以使用ThreadLocal
    private static final ThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    //设置请求线程的数据
    public static void setData(String tableName) {
        CONTEXT.set(tableName);
    }

    //删除当前请求线程的数据
    public static void removeData() {
        CONTEXT.remove();
    }

    /**
     * 1、原表名加上后缀
     * 2、直接用输入替换表名
     * 3、原表名
     * @param sql       当前执行 SQL
     * @param tableName 表名
     * @return 替换后的表名
     */
    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (!(CollectionUtils.isEmpty(tableNames)) && this.tableNames.contains(tableName)) {
            return tableName + "_" + CONTEXT.get();
        }
        if(StringUtils.hasText(CONTEXT.get())){
            return CONTEXT.get();
        }
        return tableName;
    }
}

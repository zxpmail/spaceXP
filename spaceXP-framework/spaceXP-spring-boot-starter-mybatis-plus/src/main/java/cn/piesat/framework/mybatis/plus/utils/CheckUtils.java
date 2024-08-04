package cn.piesat.framework.mybatis.plus.utils;

import cn.piesat.framework.common.exception.BaseException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p/>
 * {@code @description}: 基本检查工具类
 * <p/>
 * {@code @create}: 2024-08-02 13:34
 * {@code @author}: zhouxp
 */
public class CheckUtils {

    /**
     * 单个字段判断数据库有重复字段
     * @param checkValue 检查值
     * @param columnExtractor 行执行器函数
     * @param countExecutor  count执行器函数
     * @param errorMessage  异常提示信息模板
     * @param <T> 数据表类型
     * @param <R> 返回值类型
     * @param <V> 值类型
     */
    public static <T, R, V> void singleValueCheckRecordRepeat(V checkValue,SFunction<T, R> columnExtractor, Function<LambdaQueryWrapper<T> ,Long> countExecutor, String errorMessage) {
        if (ObjectUtils.isEmpty(checkValue)) return;
        checkRecordRepeat(m-> m.eq(columnExtractor,checkValue),countExecutor,errorMessage);
    }

    /**
     * 检查数据库是否记录重复，如果重复，则抛出异常
     *
     * @param wrapper         条件执行器
     * @param countExecutor   count查询执行器
     * @param errorMessage    异常提示信息模板
     * @param <T>             实体类类型
     */
    @SuppressWarnings("unchecked")
    public static <T> void checkRecordRepeat(Consumer<LambdaQueryWrapper<T>> wrapper, Function<LambdaQueryWrapper<T> ,Long> countExecutor, String errorMessage) {
        LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
        wrapper.accept(w);
        Long count = countExecutor.apply(w);
        if (count > 0) {
            throw new BaseException(errorMessage);
        }
    }

    /**
     *
     * @param columnExtractorAndValues map 含有值和行执行器函数
     * @param countExecutor  count执行器函数
     * @param errorMessage  异常提示信息模板
     * @param <T> 数据表类型
     * @param <R> 返回值类型
     * @param <V> 值类型
     */
    public static <T, R, V> void multiValueCheckRecordRepeat(Map<SFunction<T, R>,V> columnExtractorAndValues, Function<LambdaQueryWrapper<T> ,Long> countExecutor, String errorMessage) {
        if (CollectionUtils.isEmpty(columnExtractorAndValues)) return;
        Set<Map.Entry<SFunction<T, R>, V>> entries = columnExtractorAndValues.entrySet();
        checkRecordRepeat(m->{
            for (Map.Entry<SFunction<T, R>, V> e : entries) {
                m.eq(e.getKey(), e.getValue());
            }
        },countExecutor,errorMessage);
    }
}

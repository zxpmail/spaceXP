package cn.piesat.framework.mybatis.plus.utils;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.mybatis.plus.constants.Constants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p/>
 * {@code @description}: 检查记录重复工具类
 * <p/>
 * {@code @create}: 2024-08-02 13:34
 * {@code @author}: zhouxp
 */
public class CheckRecordRepeatUtils {


    /**
     * 单个字段判断数据库有重复字段
     *
     * @param checkValue      检查值
     * @param columnExtractor 行执行器函数
     * @param countExecutor   count执行器函数
     * @param <T>             数据表类型
     * @param <R0>            返回值类型
     * @param <V0>            值类型
     * @param <V1>            值类型
     * @param <R1>            返回值类型
     */
    public static <T, R0, V0, V1, R1> void checkRecordRepeat(V0 checkValue, SFunction<T, R1> columnExtractor, Function<LambdaQueryWrapper<T>, Long> countExecutor, V1 checkNotValue, SFunction<T, R1> columnNotExtractor) {
        checkRecordRepeat(checkValue, columnExtractor, countExecutor, checkNotValue, columnNotExtractor, Constants.ERROR_MESSAGE);
    }

    /**
     * 单个字段判断数据库有重复字段
     *
     * @param checkValue      检查值
     * @param columnExtractor 行执行器函数
     * @param countExecutor   count执行器函数
     * @param errorMessage    异常提示信息模板
     * @param <T>             数据表类型
     * @param <R0>            返回值类型
     * @param <V0>            值类型
     * @param <V1>            值类型
     * @param <R1>            返回值类型
     */
    public static <T, R0, V0, V1, R1> void checkRecordRepeat(V0 checkValue, SFunction<T, R1> columnExtractor, Function<LambdaQueryWrapper<T>, Long> countExecutor, V1 checkNotValue, SFunction<T, R1> columnNotExtractor, String errorMessage) {
        checkRecordRepeat(m -> {
            m.eq((ObjectUtils.isEmpty(checkValue)), columnExtractor, checkValue);
            m.ne(ObjectUtils.isEmpty(checkNotValue), columnNotExtractor, checkNotValue);
        }, countExecutor, errorMessage);
    }

    /**
     * 单个字段判断数据库有重复字段
     *
     * @param checkValue      检查值
     * @param columnExtractor 行执行器函数
     * @param countExecutor   count执行器函数
     * @param <T>             数据表类型
     * @param <R>             返回值类型
     * @param <V>             值类型
     */
    public static <T, R, V> void checkRecordRepeat(V checkValue, SFunction<T, R> columnExtractor, Function<LambdaQueryWrapper<T>, Long> countExecutor) {
        checkRecordRepeat(checkValue, columnExtractor, countExecutor, Constants.ERROR_MESSAGE);
    }

    /**
     * 单个字段判断数据库有重复字段
     *
     * @param checkValue      检查值
     * @param columnExtractor 行执行器函数
     * @param countExecutor   count执行器函数
     * @param errorMessage    异常提示信息模板
     * @param <T>             数据表类型
     * @param <R>             返回值类型
     * @param <V>             值类型
     */
    public static <T, R, V> void checkRecordRepeat(V checkValue, SFunction<T, R> columnExtractor, Function<LambdaQueryWrapper<T>, Long> countExecutor, String errorMessage) {
        checkRecordRepeat(m -> m.eq((ObjectUtils.isEmpty(checkValue)), columnExtractor, checkValue), countExecutor, errorMessage);
    }


    /**
     * 检查数据库是否记录重复，如果重复，则抛出异常
     *
     * @param wrapper       条件执行器
     * @param countExecutor count查询执行器
     * @param errorMessage  异常提示信息模板
     * @param <T>           实体类类型
     */

    private static <T> void checkRecordRepeat(Consumer<LambdaQueryWrapper<T>> wrapper, Function<LambdaQueryWrapper<T>, Long> countExecutor, String errorMessage) {
        LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
        wrapper.accept(w);
        Long count = countExecutor.apply(w);
        if (count > 0) {
            throw new BaseException(errorMessage);
        }
    }

    /**
     * @param columnExtractorAndValues map 含有值和行执行器函数
     * @param countExecutor            count执行器函数
     * @param errorMessage             异常提示信息模板
     * @param <T>                      数据表类型
     * @param <R>                      返回值类型
     * @param <V>                      值类型
     */
    public static <T, R, V> void checkRecordRepeat(Map<SFunction<T, R>, V> columnExtractorAndValues, Function<LambdaQueryWrapper<T>, Long> countExecutor, String errorMessage) {
        if (CollectionUtils.isEmpty(columnExtractorAndValues)) return;
        Set<Map.Entry<SFunction<T, R>, V>> entries = columnExtractorAndValues.entrySet();
        checkRecordRepeat(m -> {
            for (Map.Entry<SFunction<T, R>, V> e : entries) {
                m.eq((!ObjectUtils.isEmpty(e.getValue())), e.getKey(), e.getValue());
            }
        }, countExecutor, errorMessage);
    }

    /**
     * @param columnExtractorAndValues map 含有值和行执行器函数
     * @param countExecutor            count执行器函数
     * @param <T>                      数据表类型
     * @param <R>                      返回值类型
     * @param <V>                      值类型
     */
    public static <T, R, V> void checkRecordRepeat(Map<SFunction<T, R>, V> columnExtractorAndValues, Function<LambdaQueryWrapper<T>, Long> countExecutor) {
        checkRecordRepeat(columnExtractorAndValues, countExecutor, Constants.ERROR_MESSAGE);
    }

    /**
     * @param columnExtractorAndValues map 含有值和行执行器函数
     * @param columnNotExtractorAndValues map 不含有值和行执行器函数
     * @param countExecutor            count执行器函数
     * @param <T>             数据表类型
     * @param <R0>            返回值类型
     * @param <V0>            值类型
     * @param <V1>            值类型
     * @param <R1>            返回值类型
     */
    public static <T, R0, V0, R1, V1> void checkRecordRepeat(Map<SFunction<T, R0>, V0> columnExtractorAndValues, Map<SFunction<T, R1>, V1> columnNotExtractorAndValues, Function<LambdaQueryWrapper<T>, Long> countExecutor) {
        checkRecordRepeat(columnExtractorAndValues,columnNotExtractorAndValues, countExecutor, Constants.ERROR_MESSAGE);
    }
    /**
     * @param columnExtractorAndValues map 含有值和行执行器函数
     * @param columnNotExtractorAndValues map 不含有值和行执行器函数
     * @param countExecutor            count执行器函数
     * @param errorMessage             异常提示信息模板
     * @param <T>             数据表类型
     * @param <R0>            返回值类型
     * @param <V0>            值类型
     * @param <V1>            值类型
     * @param <R1>            返回值类型
     */
    public static <T, R0, V0, R1, V1> void checkRecordRepeat(Map<SFunction<T, R0>, V0> columnExtractorAndValues, Map<SFunction<T, R1>, V1> columnNotExtractorAndValues, Function<LambdaQueryWrapper<T>, Long> countExecutor, String errorMessage) {
        if (CollectionUtils.isEmpty(columnExtractorAndValues) && CollectionUtils.isEmpty(columnNotExtractorAndValues))
            return;
        checkRecordRepeat(m -> {
            if (!CollectionUtils.isEmpty(columnExtractorAndValues)) {
                for (Map.Entry<SFunction<T, R0>, V0> e : columnExtractorAndValues.entrySet()) {
                    m.eq((!ObjectUtils.isEmpty(e.getValue())), e.getKey(), e.getValue());
                }
            }
            if (!CollectionUtils.isEmpty(columnNotExtractorAndValues)) {
                for (Map.Entry<SFunction<T, R1>, V1> e : columnNotExtractorAndValues.entrySet()) {
                    m.ne((!ObjectUtils.isEmpty(e.getValue())), e.getKey(), e.getValue());
                }
            }
        }, countExecutor, errorMessage);
    }
}

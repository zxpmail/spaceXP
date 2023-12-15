package cn.piesat.framework.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  : 复制一个bean或bean列表 并返回一个目标bean或列表
 * 例子：
 *   TestVO vo = CopyBean.copy(testDO,TestVO::new);
 *   List<TestVO> vos = CopyBean.copy(List<TestDO> testDO,TestVO::new);
 * <p/>
 * <b>@create:</b> 2023/9/19 17:45.
 *
 * @author zhouxp
 */
public class CopyBeanUtils {
    /**
     * 从源bean中复制到目标bean中
     *
     * @param source :源bean
     * @param target :目标类型对象
     * @param <T>    目标类型
     * @param <S>源类型
     * @return 返回目标bean
     */
    public static <T, S> T copy(S source, Supplier<T> target) {
        if(ObjectUtils.isEmpty(source)){
            return null;
        }
        T t = target.get();
        BeanUtils.copyProperties(source, t, getNullPropertyNames(source));
        return t;
    }

    /**
     * 从源bean list集合中复制到目标bean List集合中
     *
     * @param source 源bean List集合
     * @param target 目标类型对象
     * @param <T>    目标类型
     * @param <S>    源类型
     * @return 返回目标bean List集合
     */
    public static <T, S> List<T> copy(List<S> source, Supplier<T> target) {
        if(CollectionUtils.isEmpty(source)){
            return new ArrayList<>();
        }
        return source.stream()
                .map(u -> CopyBeanUtils.copy(u, target))
                .collect(Collectors.toList());
    }

    /**
     * 得到实体类中为空的字段，进行BeanCopy忽略
     *
     * @param source 要拷贝的实体类
     * @return 为空的字段
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}

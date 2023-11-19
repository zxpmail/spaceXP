package cn.piesat.framework.mybatis.plus.utils;

import cn.piesat.framework.common.model.dto.PageBean;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p/>
 * {@code @description}  :查询工具类
 * <p/>
 * <b>@create:</b> 2023/10/7 13:52.
 *
 * @author zhouxp
 */
public class QueryUtils {
    /**
     * 包装IPage接口
     * @param pageBean 分页bean
     * @return 新的IPage接口
     * @param <T> 实体类
     */
    public static<T> IPage<T> getPage(PageBean pageBean) {
        return new Page<>(pageBean.getPage(), pageBean.getSize());
    }
}

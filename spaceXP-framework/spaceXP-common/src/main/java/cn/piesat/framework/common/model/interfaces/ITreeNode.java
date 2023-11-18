package cn.piesat.framework.common.model.interfaces;

import java.util.List;


/**
 * <p/>
 * {@code @description}  :生成树形接口类
 * 生成树必须继承此类，实体类属性必须包含id，pid及children字段类型
 * <p/>
 * <b>@create:</b> 2023/9/22 8:48.
 *
 * @author zhouxp
 */
public interface ITreeNode<E,V> {
    /**
     * 获取祖级字符串
     * @return 返回祖级字符串
     */
    default String getAncestors(){
        return "";
    }
    /**
     * 节点ID
     */
    V getId();
    /**
     * 父节点ID
     */
    V getPid();

    /**
     * 设置孩子节点
     */
    void setChildren(List<E> children);
    /**
     * 获取孩子节点
     */
    List<E> getChildren();

}

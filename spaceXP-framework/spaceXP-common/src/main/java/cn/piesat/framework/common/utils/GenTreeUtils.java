package cn.piesat.framework.common.utils;

import cn.piesat.framework.common.model.interfaces.ITreeNode;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  :生成树工具类
 * 大多数生生成树是list列表方式，所以生成树只做list方式
 * example：
 *
 * @author zhouxp
 * @Data private static class MenuVo implements ITreeNode<MenuVo, Integer> {
 * private Integer id;
 * private String name;
 * private Integer pid;
 * private List<MenuVo> children;
 * public MenuVo(Integer id, String name, Integer parentId) {
 * this.id = id;
 * this.name = name;
 * this.pid = parentId;
 * }
 * }
 * }
 * List<MenuVo> menusVoList = new ArrayList<>(Arrays.asList(
 * new MenuVo(1, "A公司", 0),
 * new MenuVo(2, "a销售部", 14),
 * new MenuVo(3, "财税部", 1),
 * new MenuVo(4, "商务部", 1),
 * new MenuVo(5, "综合部", 1),
 * new MenuVo(6, "a销售1部", 2),
 * new MenuVo(7, "a销售2部", 2),
 * new MenuVo(8, "a销售3部", 2),
 * new MenuVo(9, "a销售4部", 2),
 * new MenuVo(10, "b销售部", 14),
 * new MenuVo(11, "b销售1部", 10),
 * new MenuVo(12, "b销售2部", 10),
 * new MenuVo(13, "人事部", 1),
 * new MenuVo(14, "销售部", 1)));
 * // 组装树形结构
 * List<MenuVo> menus = GenTreeUtil.buildTree(menusVoList, 0);
 * <p/>
 * <b>@create:</b> 2023/9/22 9:00.
 */
public class GenTreeUtils {

    /**
     * 从树中查询pid的节点及父节点
     *
     * @param tree 树形结构
     * @param pid  父节点id
     * @param <E>  节点类型
     * @param <V>  父节点id类型
     * @return 树形结构列表
     */
    public static <E extends ITreeNode<E, V>, V> List<E> search(List<E> tree, V pid) {
        return search(tree, x -> x.getPid().equals(pid), ITreeNode::getChildren);
    }

    /**
     * 根据所有树节点列表，按默认条件生成含有所有树形结构的列表
     * 主要用于组建简单树形结构
     *
     * @param nodes 树形节点列表
     * @param pid   父节点id
     * @param <E>   节点类型
     * @param <V>   父节点id类型
     * @return 树形结构列表
     */
    public static <E extends ITreeNode<E, V>, V> List<E> buildTree(List<E> nodes, V pid) {
        return makeTree(nodes, ITreeNode::getPid, ITreeNode::getId, x -> x.getPid().equals(pid), ITreeNode::setChildren);
    }

    /**
     * 使用Map合成树
     * 参考 <a href="https://juejin.cn/post/7398047016183889935#heading-13">...</a>
     *
     * @param menuList       需要合成树的List
     * @param pId            对象中的父ID字段,如:Menu:getPid
     * @param id             对象中的id字段 ,如：Menu:getId
     * @param rootCheck      判断E中为根节点的条件，如：x->x.getPId()==-1L , x->x.getParentId()==null,x->x.getParentMenuId()==0
     * @param setSubChildren E中设置下级数据方法，如： Menu::setSubMenus
     * @param <T>            ID字段类型
     * @param <E>            泛型实体对象
     * @return 树形结构列表
     */
    public static <T, E> List<E> makeTree(List<E> menuList, Function<E, T> pId, Function<E, T> id,
                                          Predicate<E> rootCheck, BiConsumer<E, List<E>> setSubChildren) {
        // 参数校验
        validateParameters(menuList, pId, id, rootCheck, setSubChildren);

        Map<Optional<T>, List<E>> parentMenuMap = menuList.stream().collect(Collectors.groupingBy(
                node -> Optional.ofNullable(pId.apply(node)),
                Collectors.toList()
        ));

        List<E> result = new ArrayList<>();
        for (E node : menuList) {
            if (id.apply(node) != null) {
                setSubChildren.accept(node, parentMenuMap.get(Optional.ofNullable(id.apply(node))));
            }
            // 如果是根节点，加入结构
            if (rootCheck.test(node)) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 验证输入参数
     */
    private static <T, E> void validateParameters(List<E> menuList, Function<E, T> pId, Function<E, T> id,
                                                  Predicate<E> rootCheck, BiConsumer<E, List<E>> setSubChildren) {
        if (menuList == null || pId == null || id == null || rootCheck == null || setSubChildren == null) {
            throw new IllegalArgumentException("menuList and functional parameters must not be null");
        }
    }

    /**
     * 参见 <a href="https://juejin.cn/post/7398047016183889935#heading-13">...</a>
     * 树中搜索：注意会影响原来tree结构
     *
     * @param tree           需要搜索的树
     * @param predicate      过滤条件
     * @param getSubChildren 获取下级数据方法，如：MenuVo::getSubMenus
     * @param <E>            泛型实体对象
     * @return 返回搜索到的节点及其父级到根节点
     */
    public static <E> List<E> search(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getSubChildren) {
        Iterator<E> iterator = tree.iterator();
        while (iterator.hasNext()) {
            E item = iterator.next();
            List<E> childList = getSubChildren.apply(item);
            if (childList != null && !childList.isEmpty()) {
                search(childList, predicate, getSubChildren);
            }
            if (!predicate.test(item) && (childList == null || childList.isEmpty())) {
                iterator.remove();
            }
        }
        return tree;
    }

    /**
     * 把树打平功能
     * @param tree 树形结构
     * @param getSubChildren  获取下级数据方法，如：MenuVo::getSubMenus
     * @param predicate 过滤条件
     * @return 返回list集合
     * @param <E> 泛型实体对象
     */
    public static <E> List<E> treeToList(List<E> tree, Function<E, List<E>> getSubChildren, Predicate<E> predicate) {
        if (tree.isEmpty()) {
            return new ArrayList<>();
        }
        List<E> resultList = new ArrayList<>();
        for (E node : tree) {
            List<E> children = getSubChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                resultList.addAll(treeToList(children, getSubChildren,predicate));
            }
            if(predicate.test(node)) {
                resultList.add(node);
            }
        }
        return resultList;
    }
}

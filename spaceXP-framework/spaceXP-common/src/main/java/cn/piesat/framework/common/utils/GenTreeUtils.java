package cn.piesat.framework.common.utils;


import cn.piesat.framework.common.model.interfaces.ITreeNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;


import java.util.Collections;
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
@Slf4j
public class GenTreeUtils {

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
     * @param tree 树形结构
     * @param <E>  节点类型
     * @return 打平的List
     */
    public static <E extends ITreeNode<E, V>, V> List<E> treeToList(List<E> tree) {
        return treeToList(tree, ITreeNode::getChildren, ITreeNode::setChildren);
    }

    /**
     * 使用Map合成树 (注意会改变原来menuList值)
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
     * 把树打平功能
     *
     * @param tree           树形结构 (注意会改变原来tree值)
     * @param getSubChildren 获取下级数据方法，如：MenuVo::getSubMenus
     * @param setSubChildren 设置下级数据方法，如：MenuVo:setSubMenus
     * @param <E>            泛型实体对象
     * @return 返回list集合
     */
    public static <E> List<E> treeToList(List<E> tree, Function<E, List<E>> getSubChildren, BiConsumer<E, List<E>> setSubChildren) {
        if (tree.isEmpty()) {
            return new ArrayList<>();
        }
        List<E> resultList = new ArrayList<>();
        for (E node : tree) {
            List<E> children = getSubChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                resultList.addAll(treeToList(children, getSubChildren, setSubChildren));
            }
            setSubChildren.accept(node, null);
            resultList.add(node);
        }
        return resultList;
    }

    /**
     * 在树中查询符合条件节点以及其父节点
     * 例如：@Data
     *     static class TreeNode {
     *         int val;
     *         List<TreeNode> children;
     *
     *         TreeNode(int x) {
     *             val = x;
     *             children = new ArrayList<>();
     *         }
     *     }
     *         TreeNode root = new TreeNode(1);
     *         TreeNode node2 = new TreeNode(5);
     *         TreeNode node3 = new TreeNode(3);
     *         TreeNode node4 = new TreeNode(4);
     *         TreeNode node5 = new TreeNode(5);
     *
     *         root.children.add(node2);
     *         root.children.add(node3);
     *         node2.children.add(node4);
     *         node2.children.add(node5);
     *
     *         // 查找所有路径
     *
     *         List<List<TreeNode>> paths = findAllNodes(root, x -> x.val == 5, TreeNode::getChildren);
     *         for (List<TreeNode> path : paths) {
     *             for (TreeNode n : path) {
     *                 System.out.print(n.val + " ");
     *             }
     *             System.out.println();
     *         }
     *     }
     * @param root           根节点
     * @param predicate      判断条件
     * @param getSubChildren 当前树的子节点
     * @param <E>            树类型
     */
    public static <E> List<List<E>> findAllNodes(E root, Predicate<E> predicate, Function<E, List<E>> getSubChildren) {
        if (root == null || getSubChildren == null) {
            return null;
        }
        List<List<E>> allNodes = new ArrayList<>();
        findNodes(root, predicate, getSubChildren, new ArrayList<>(), allNodes);
        return allNodes;
    }

    /**
     * 查找树，当找到节点就保存此节点到根上的节点，没有查询到就不保存
     *
     * @param node           查询的树节点
     * @param predicate      判断条件
     * @param getSubChildren 当前树的子节点
     * @param currentNodes   查找树一次结果节点列表
     * @param allNodes       查询节点集合
     * @param <E>            树类型
     */
    private static <E> void findNodes(E node, Predicate<E> predicate, Function<E, List<E>> getSubChildren, List<E> currentNodes, List<List<E>> allNodes) {
        if (node == null || getSubChildren == null) {
            return;
        }
        try {
            currentNodes.add(node);
            if (predicate.test(node)) {
                allNodes.add(Collections.unmodifiableList(new ArrayList<>(currentNodes)));
            }
            List<E> children = getSubChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                processChildren(children, predicate, getSubChildren, currentNodes, allNodes);
            }
        } catch (Exception e) {
            log.error("Error processing node {}", node, e);
        } finally {
            currentNodes.remove(currentNodes.size() - 1);
        }
    }

    /**
     * 遍历处理子节点
     * @param children       子节点
     * @param predicate      判断条件
     * @param getSubChildren 当前树的子节点
     * @param currentNodes   查找树一次结果节点列表
     * @param allNodes       查询节点集合
     * @param <E>            树类型
     */
    private static <E> void processChildren(List<E> children, Predicate<E> predicate, Function<E, List<E>> getSubChildren, List<E> currentNodes, List<List<E>> allNodes) {
        for (E child : children) {
            findNodes(child, predicate, getSubChildren, currentNodes, allNodes);
        }
    }
}

package cn.piesat.framework.common.utils;

import cn.piesat.framework.common.model.interfaces.ITreeNode;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  :生成树工具类
 * 大多数生生成树是list列表方式，所以生成树只做list方式
 * example：
 *     @Data
 *     private static class MenuVo implements ITreeNode<MenuVo, Integer> {
 *         private Integer id;
 *         private String name;
 *         private Integer pid;
 *         private List<MenuVo> children;
 *         public MenuVo(Integer id, String name, Integer parentId) {
 *             this.id = id;
 *             this.name = name;
 *             this.pid = parentId;
 *         }
 *     }
 * }
 * List<MenuVo> menusVoList = new ArrayList<>(Arrays.asList(
 *                 new MenuVo(1, "A公司", 0),
 *                 new MenuVo(2, "a销售部", 14),
 *                 new MenuVo(3, "财税部", 1),
 *                 new MenuVo(4, "商务部", 1),
 *                 new MenuVo(5, "综合部", 1),
 *                 new MenuVo(6, "a销售1部", 2),
 *                 new MenuVo(7, "a销售2部", 2),
 *                 new MenuVo(8, "a销售3部", 2),
 *                 new MenuVo(9, "a销售4部", 2),
 *                 new MenuVo(10, "b销售部", 14),
 *                 new MenuVo(11, "b销售1部", 10),
 *                 new MenuVo(12, "b销售2部", 10),
 *                 new MenuVo(13, "人事部", 1),
 *                 new MenuVo(14, "销售部", 1)));
 *
 *         // 组装树形结构
 *         List<MenuVo> menus = GenTreeUtil.buildTree(menusVoList, 0);
 * <p/>
 * <b>@create:</b> 2023/9/22 9:00.
 *
 * @author zhouxp
 */
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
        Assert.notNull(nodes,"nodes 不能为空！");
        return nodes.stream().filter((p) -> p.getPid().equals(pid)).peek((p) -> {
            List<E> tree = buildTree(nodes, p.getId());
            if (!CollectionUtils.isEmpty(tree)) {
                p.setChildren(tree);
            }

        }).collect(Collectors.toList());
    }
}

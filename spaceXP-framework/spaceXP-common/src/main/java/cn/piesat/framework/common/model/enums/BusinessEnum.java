package cn.piesat.framework.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  :业务操作枚举类型
 * <p/>
 * <b>@create:</b> 2022/11/30 10:18.
 *
 * @author zhouxp
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum BusinessEnum {
    /**
     * 其他
     */
    OTHER(0,"其他"),

    /**
     * 新增
     */
    INSERT(1,"新增"),

    /**
     * 修改
     */
    UPDATE(2,"修改"),

    /**
     * 删除
     */
    DELETE(3,"删除"),

    /**
     * 授权
     */
    GRANT(4,"授权"),

    /**
     * 导出
     */
    EXPORT(5,"导出"),

    /**
     * 导入
     */
    IMPORT(6,"导入"),

    /**
     * 清空数据
     */
    CLEAN(7,"清空数据"),
    /**
     * 查询数据
     */
    SELECT(8,"查询"),
    /**
     * 系统登录
     */
    LOGIN(9,"登录"),
    /**
     * 系统登出
     */

    LOGOUT(10,"登出"),

    INFO(11,"详情"),
    ;
    /**
     * 操作代码
     */
    private Integer code;
    /**
     * 操作
     */
    private String op;
}

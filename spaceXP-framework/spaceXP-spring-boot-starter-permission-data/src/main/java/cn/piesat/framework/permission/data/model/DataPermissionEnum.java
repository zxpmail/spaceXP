package cn.piesat.framework.permission.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p/>
 * {@code @description}  :用户数据权限范围枚举类
 * <p/>
 * <b>@create:</b> 2023/9/6 10:50.
 *
 * @author zhouxp
 */
@Getter
@AllArgsConstructor
public enum DataPermissionEnum {
    SELF_SCOPE(1, "自身数据权限"),
    DEPT_SCOPE(2, "部门数据权限"),
    DEPT_SUB_SCOPE(3, "部门及以下数据权限"),
    ALL_SCOPE(4, "全部数据权限"),

    NO_SCOPE(5, "没权限！");
    private final int code;
    private final String info;

    public DataPermissionEnum getEnumByCode(int code) {
        for (DataPermissionEnum myEnum : DataPermissionEnum.values()) {
            if (myEnum.getCode() == code) {
                return myEnum;
            }
        }
        throw new IllegalArgumentException("无法找到与code值匹配的枚举实例");
    }
}

package cn.piesat.framework.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  : 用于JWT生成，检查等功能
 * <p/>
 * <b>@create:</b> 2023/9/22 10:55.
 *
 * @author zhouxp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser {
    /**
     * 用户ID信息
     */
    private Long userId;
    /**
     * 部门id信息
     */
    private Long deptId;
    /**
     * 用户名称信息
     */
    private String userName;
    /**
     * 部门名称信息
     */
    private String deptName;
    /**
     * 多租户ID信息
     */
    private Long tenantId;
}

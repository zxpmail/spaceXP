package cn.piesat.framework.permission.data.core;


import cn.piesat.framework.permission.data.model.DataPermissionEnum;
import cn.piesat.framework.permission.data.model.UserDataPermission;
import cn.piesat.framework.permission.data.properties.DataPermissionProperties;
import cn.piesat.framework.permission.data.utils.DataPermissionContextHolder;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  : 权限处理拦截器
 * <p/>
 * <b>@create:</b> 2023/9/6 9:16.
 *
 * @author zhouxp
 */
@Slf4j
@Data
public class DataPermissionHandler implements MultiDataPermissionHandler {
    private final DataPermissionProperties dataPermissionProperties;

    public DataPermissionHandler(DataPermissionProperties dataPermissionProperties) {
        this.dataPermissionProperties = dataPermissionProperties;
    }

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mid) {
        // 1. 如果没有获取用户数据权限信息、包含忽略sql或用户直接返回不进行数据权限处理
        UserDataPermission userDataScope = DataPermissionContextHolder.getUserDataPermission();
        if (ObjectUtils.isEmpty(userDataScope) || ObjectUtils.isEmpty(dataPermissionProperties)) {
            return where;
        }
        Set<String> ignoreUsers = dataPermissionProperties.getIgnoreUsers();
        if (!CollectionUtils.isEmpty(ignoreUsers)) {
            if (ignoreUsers.contains(userDataScope.getUsername())) {
                return where;
            }
        }
        Set<String> ignoreSql = dataPermissionProperties.getIgnoreConditions();
        if (!CollectionUtils.isEmpty(ignoreSql)) {
            for (String s : ignoreSql) {
                if (mid.contains(s)) {
                    return where;
                }
            }
        }
        Expression expression = null;
        DataPermissionEnum dataPermissionEnum = DataPermissionEnum.DEPT_SUB_SCOPE.getEnumByCode(userDataScope.getDataScope());
        // 2. 数据权限处理
        switch (dataPermissionEnum) {
            case ALL_SCOPE:
                return where;
            case SELF_SCOPE:
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new Column(dataPermissionProperties.getCreatorIdColumnName()));
                equalsTo.setRightExpression(new LongValue(userDataScope.getUserId()));
                expression = ObjectUtils.isEmpty(where) ? equalsTo : new AndExpression(where, equalsTo);
                break;
            case DEPT_SCOPE:
            case DEPT_SUB_SCOPE:
                ItemsList itemsList = new ExpressionList(userDataScope.getDeptIds().stream().map(LongValue::new).collect(Collectors.toList()));
                InExpression inExpression = new InExpression(new Column(dataPermissionProperties.getDeptIdColumnName()), itemsList);
                expression = ObjectUtils.isEmpty(where) ? inExpression : new AndExpression(where, inExpression);
                break;
            default:
                throw new RuntimeException("权限错误！");
        }
        return expression;
    }
}

package cn.piesat.framework.permission.data.core;


import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.permission.data.model.DataPermissionEnum;
import cn.piesat.framework.permission.data.model.UserDataPermission;
import cn.piesat.framework.permission.data.properties.DataPermissionProperties;
import cn.piesat.framework.permission.data.utils.DataPermissionContextHolder;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
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
public class CustomDataPermissionHandler implements DataPermissionHandler {
    private final DataPermissionProperties dataPermissionProperties;

    public CustomDataPermissionHandler(DataPermissionProperties dataPermissionProperties) {
        this.dataPermissionProperties = dataPermissionProperties;
    }

    @Override
    public Expression getSqlSegment(Expression where, String mid) {

        UserDataPermission userDataScope = DataPermissionContextHolder.getUserDataPermission();
        /*
          内部线程直接调用不进行数据权限拦截直接返回
         */
        if (Objects.isNull(userDataScope)) {
            return where;
        }
        if (!ObjectUtils.isEmpty(dataPermissionProperties)) {
            Set<String> ignoreUsers = dataPermissionProperties.getIgnoreUsers();
            if (!CollectionUtils.isEmpty(ignoreUsers)) {
                if ((!ObjectUtils.isEmpty(userDataScope)) && StringUtils.hasText(userDataScope.getUsername()) && ignoreUsers.contains(userDataScope.getUsername())) {
                    return where;
                }
            }
            //没在条件中直接返回
            Set<String> sql = dataPermissionProperties.getConditions();
            if (!CollectionUtils.isEmpty(sql)) {
                boolean isExist = false;
                for (String s : sql) {
                    if (mid.contains(s)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    return where;
                }
            } else {
                Set<String> ignoreSql = dataPermissionProperties.getIgnoreConditions();
                if (!CollectionUtils.isEmpty(ignoreSql)) {
                    for (String s : ignoreSql) {
                        if (mid.contains(s)) {
                            return where;
                        }
                    }
                }
            }
        }

        Expression expression = null;
        DataPermissionEnum dataPermissionEnum = DataPermissionEnum.NO_SCOPE;
        if (!ObjectUtils.isEmpty(userDataScope) && !ObjectUtils.isEmpty(userDataScope.getDataScope())) {
            dataPermissionEnum = DataPermissionEnum.DEPT_SUB_SCOPE.getEnumByCode(userDataScope.getDataScope());
        }
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
                List<Expression> collect = userDataScope.getDeptIds().stream().map(m -> {
                    if (m instanceof Long) {
                        return new LongValue((Long) m);
                    } else {
                        return new StringValue((String) m);
                    }
                }).collect(Collectors.toList());

                ItemsList itemsList = new ExpressionList(collect);
                InExpression inExpression = new InExpression(new Column(dataPermissionProperties.getDeptIdColumnName()), itemsList);
                expression = ObjectUtils.isEmpty(where) ? inExpression : new AndExpression(where, inExpression);
                break;
            default:
                throw new BaseException(CommonResponseEnum.NO_PERMISSION_DATA);

        }
        return expression;
    }
}

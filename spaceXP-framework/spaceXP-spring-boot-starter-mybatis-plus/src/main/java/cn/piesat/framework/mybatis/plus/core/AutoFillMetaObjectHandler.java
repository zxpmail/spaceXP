package cn.piesat.framework.mybatis.plus.core;

import cn.piesat.framework.common.constants.CommonConstants;

import cn.piesat.framework.common.utils.ServletUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>自定义字段自动填充处理类 - 实体类中使用 @TableField注解</p>
 *
 * @author :zhouxp
 * {@code @date} 2022/9/28 9:25
 * {@code @description} : 自动填充创建者ID，更新者ID，部门ID，多租户ID，创建时间，更新时间
 */
@Slf4j
@RequiredArgsConstructor
@Primary
public class AutoFillMetaObjectHandler implements MetaObjectHandler {


    /**
     * @Autowired( required = false)当 没有实现AutoFillMetaObjectService时
     * 自动注入不出错
     */
    @Autowired(required = false)
    private  AutoFillMetaObjectService autoFillMetaObjectService;
    /**
     * 创建时间字段
     */
    private final String createTime;

    /**
     * 更新时间字段
     */
    private final String updateTime;

    /**
     * 创建者ID字段
     */
    private final String createId;

    /**
     * 更新者ID字段
     */
    private final String updateId;

    /**
     * 部门ID字段
     */
    private final String deptId;

    /**
     * 多租户ID字段
     */
    private final String tenantId;

    /**
     * 逻辑删除标志
     */
    private final String deleted;

    @Value("${mybatis-plus.global-config.db-config.logic-not-delete-value:0}")
    private Integer deleteInitValue;

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter(createTime) && metaObject.hasSetter(createTime)) {
            this.strictInsertFill(metaObject, createTime, LocalDateTime::now, LocalDateTime.class);
        }
        if (metaObject.hasGetter(updateTime) && metaObject.hasSetter(updateTime)) {
            this.strictInsertFill(metaObject, updateTime, LocalDateTime::now, LocalDateTime.class);
        }
        if (metaObject.hasGetter(deleted) && metaObject.hasSetter(deleted)) {
            metaObject.setValue(deleted, deleteInitValue);
        }
        HttpServletRequest request = ServletUtils.getRequest();
        fillValue(metaObject,createId, request,CommonConstants.USER_ID);
        fillValue(metaObject,updateId, request,CommonConstants.USER_ID);
        fillValue(metaObject,deptId, request,CommonConstants.DEPT_ID);
        fillValue(metaObject,tenantId, request,CommonConstants.TENANT_ID);
        if(!ObjectUtils.isEmpty(autoFillMetaObjectService)){
            autoFillMetaObjectService.insertFill(this,metaObject);
        }

    }

    private void fillValue(MetaObject metaObject, String field,HttpServletRequest request,String constantValue) {
        String id = "-1";
        if (!ObjectUtils.isEmpty(request)) {
            id = request.getHeader(constantValue);
            if(!StringUtils.hasText(id)){
                id = "-1";
            }
        }
        if (metaObject.hasGetter(field) && metaObject.hasSetter(field)) {
            this.fillStrategy(metaObject, field, id);
        }
    }



    @Override
    public void updateFill(MetaObject metaObject) {
        HttpServletRequest request = ServletUtils.getRequest();
        String userId = "-1";
        if (!ObjectUtils.isEmpty(request)) {
            userId = request.getHeader(CommonConstants.USER_ID);
            if(!StringUtils.hasText(userId)){
                userId = "-1";
            }
        }
        if (metaObject.hasGetter(updateTime) && metaObject.hasSetter(updateTime)) {
            metaObject.setValue(updateTime, null);
            this.strictUpdateFill(metaObject, updateTime, LocalDateTime::now, LocalDateTime.class);
        }
        if (metaObject.hasGetter(updateId) && metaObject.hasSetter(updateId)) {
            metaObject.setValue(updateId, null);
            this.setFieldValByName(updateId, userId, metaObject);
        }

        /**
         * 增加外部填充
         */
        if(!ObjectUtils.isEmpty(autoFillMetaObjectService)){
            autoFillMetaObjectService.updateFill(this,metaObject);
        }
    }
}

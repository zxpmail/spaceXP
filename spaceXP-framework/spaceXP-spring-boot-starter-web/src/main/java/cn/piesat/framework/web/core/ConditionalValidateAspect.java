package cn.piesat.framework.web.core;

import cn.piesat.framework.web.annotation.ConditionalValidateField;
import cn.piesat.framework.web.constants.Constants;
import cn.piesat.framework.web.core.impl.RelationFieldNotNullHandleImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p/>
 * {@code @description}: 条件校验切面
 * <p/>
 * {@code @create}: 2025-02-26 17:15:00
 * {@code @author}: zhouxp
 */
@Aspect
public class ConditionalValidateAspect implements InitializingBean {

    /**
     * 将方法参数纳入Spring管理
     */
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 解析spel表达式
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    private final Map<Integer, ValidateHandle> validateFieldActionHandleMapping = new HashMap<>();


    @Resource
    private ApplicationContext applicationContext;
    @Override
    public void afterPropertiesSet() throws Exception {
        validateFieldActionHandleMapping.put(Constants.IF_EQ_NOT_NULL, applicationContext.getBean(RelationFieldNotNullHandleImpl.class));
    }

    @Before("@annotation(cn.piesat.framework.web.annotation.ConditionalValidate)")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        try {
            // 获取参数对象数组
            Object[] args = joinPoint.getArgs();
            Assert.notEmpty(args, "没有参数");
            Assert.isTrue(args.length <= 1, "只能有一个参数");

            // 获取方法
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

            // 获取方法参数名
            String[] paramsName = discoverer.getParameterNames(method);
            // 将参数纳入Spring管理
            EvaluationContext context = new StandardEvaluationContext();
            if (paramsName != null) {
                for (int i = 0; i < paramsName.length; i++) {
                    context.setVariable(paramsName[i], args[i]);
                }
            }

            Object firstParams = args[0];
            if (firstParams != null) {
                List<Field> allFields = getAllFields(firstParams);

                // 缓存字段和注解信息
                Map<String, Class<?>> fieldClzMap = new HashMap<>();
                // 把要校验的找到
                List<ConditionalValidateFieldInfo> validateFieldList = new ArrayList<>();
                // 字段类型
                findAnnotationFieldAndClass(allFields, fieldClzMap, validateFieldList);


                // 执行校验动作，这块要分很多种情况处理
                validateFieldList.forEach(conditionalValidateFieldInfo -> {
                    if (Objects.nonNull(conditionalValidateFieldInfo)) {
                        ConditionalValidateField conditionalValidateField = conditionalValidateFieldInfo.getConditionalValidateField();
                        doValidate(conditionalValidateField, fieldClzMap, parser, conditionalValidateFieldInfo, context, paramsName);
                    }
                });
            }
        } catch (Exception e) {
            // 记录日志或返回友好错误信息
            throw new RuntimeException("条件验证失败: " + e.getMessage(), e);
        }
    }

    private void doValidate(ConditionalValidateField conditionalValidateField, Map<String, Class<?>> fieldClzMap, ExpressionParser parser, ConditionalValidateFieldInfo conditionalValidateFieldInfo, EvaluationContext context, String[] paramsName) {
        ValidateHandle validateHandle = validateFieldActionHandleMapping.get(conditionalValidateField.action());
        Assert.isTrue(!Objects.isNull(validateHandle), "不能处理的类型" + conditionalValidateField.action());
        validateHandle.doValidate(conditionalValidateField, fieldClzMap, parser, conditionalValidateFieldInfo, context, paramsName);
    }


    private void findAnnotationFieldAndClass(List<Field> allFields, Map<String, Class<?>> fieldClzMap, List<ConditionalValidateFieldInfo> validateFieldList) {
        allFields.forEach(field -> {
            Set<ConditionalValidateField> conditionalValidateFields = AnnotationUtils.getRepeatableAnnotations(field,ConditionalValidateField.class);
            String fieldName = field.getName();
            for (ConditionalValidateField conditionalValidateField : conditionalValidateFields) {
                if (Objects.nonNull(conditionalValidateField)) {
                    validateFieldList.add(new ConditionalValidateFieldInfo(fieldName, conditionalValidateField));
                }
            }
            fieldClzMap.put(fieldName, field.getType());
        });
    }

    public static List<Field> getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }
}

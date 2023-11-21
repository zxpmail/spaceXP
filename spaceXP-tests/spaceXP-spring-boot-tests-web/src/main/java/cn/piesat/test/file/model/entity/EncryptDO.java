package cn.piesat.test.file.model.entity;

import cn.piesat.framework.security.annotation.EncryptField;
import lombok.Data;

/**
 * <p/>
 * {@code @description}  :测试加密实体类
 * <p/>
 * <b>@create:</b> 2023/11/21 13:59.
 *
 * @author zhouxp
 */
@Data
public class EncryptDO {
    @EncryptField
    private  String name;
    private Integer age;
}

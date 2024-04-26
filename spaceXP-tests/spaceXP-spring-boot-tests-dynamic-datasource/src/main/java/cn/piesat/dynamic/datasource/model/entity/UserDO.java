package cn.piesat.dynamic.datasource.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/12/7 16:00.
 *
 * @author zhouxp
 */
@Data
@TableName("user")
public class UserDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
}

package cn.piesat.tests.mybaits.plus.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-09-18 13:08
 * {@code @author}: zhouxp
 */
@TableName("t_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDO {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    private String name;

}

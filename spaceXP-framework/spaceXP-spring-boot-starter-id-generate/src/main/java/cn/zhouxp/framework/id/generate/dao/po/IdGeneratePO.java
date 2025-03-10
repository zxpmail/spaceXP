package cn.zhouxp.framework.id.generate.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-05 17:36:42
 *
 * @author zhouxp
 */
@Data
@TableName("sys_id_generate")
public class IdGeneratePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 步长
     */
    private int step;

    /**
     * 1 有序 0无序
     */
    private int ordered;

    /**
     * 当前id所在阶段的开始值
     */
    private long start;

    /**
     * 当前id所在阶段结束值
     */
    private long end;

    /**
     * 业务
     */
    private String biz;

    /**
     * id备注描述
     */
    private String remark;

    /**
     * 乐观锁版本号
     */
    private int version;

    private LocalDateTime  createTime;

    private LocalDateTime updateTime;
}

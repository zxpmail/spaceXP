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
@TableName("t_id_generate")
public class IdGeneratePO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 初始化值
     */
    private long initNum;

    /**
     * 步长
     */
    private int step;

    /**
     * 是否是有序的id
     */
    private int isSeq;

    /**
     * 当前id所在阶段的开始值
     */
    private long currentStart;

    /**
     * 当前id所在阶段的阈值
     */
    private long nextThreshold;

    /**
     * 业务代码前缀
     */
    private String idPrefix;

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

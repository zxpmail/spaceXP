package cn.piesat.framework.file.s3.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 * @author :zhouxp
 * {@code @date} 2022/9/30 16:12
 * {@code @description} :
 */
@Getter
@AllArgsConstructor
public enum S3ResponseEnum implements IBaseResponse {
    OSS_SET_ERROR(520,"OSS配置错误，Endpoint,secret,key配置不能为空，请检查"),
    ;

    /**
     * 响应状态码
     */
    private final Integer code;

    /**
     * 响应信息
     */
    private final   String message;
}

package cn.piesat.framework.file.s3.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :OSS配置信息
 * <p/>
 * <b>@create:</b> 2022/12/13 9:48.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.oss")
@Data
public class OssProperties {
    private String accessKey;
    private String accessSecret;
    /**
     * 如果是服务器MinIO等直接使用 [$schema]://[$ip]:[$port]
     * 外网[$Schema]://[$Bucket].[$Endpoint]/[$Object]*
     * https://help.aliyun.com/document_detail/375241.html*
     */
    private String endpoint;
    /**
     * refres to com.amazonaws.regions.Regions*
     * https://help.aliyun.com/document_detail/31837.htm?spm=a2c4g.11186623.0.0.695178eb0nD6jp*
     */
    private String region;
    private boolean pathStyleAccess = true;
    private String bucketName;
}

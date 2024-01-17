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
    /**
     * Access key
     */
    private String accessKey;
    /**
     * Secret key
     */
    private String accessSecret;
    /**
     * 对象存储服务的URL
     * 如果是服务器MinIO等直接使用 [$schema]://[$ip]:[$port]
     * 外网[$Schema]://[$Bucket].[$Endpoint]/[$Object]*
     * https://help.aliyun.com/document_detail/375241.html*
     */
    private String endpoint;
    /**
     * 区域
     * refres to com.amazonaws.regions.Regions*
     * https://help.aliyun.com/document_detail/31837.htm?spm=a2c4g.11186623.0.0.695178eb0nD6jp*
     */
    private String region;
    /**
     * true path-style nginx 反向代理和S3默认支持 pathStyle模式 {http://endpoint/bucketname}
     * false supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style 模式{http://bucketname.endpoint}
     * 只是url的显示不一样
     */
    private boolean pathStyleAccess = true;
    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 最大线程数，默认：100
     */
    private Integer maxConnections = 100;
}

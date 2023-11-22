package cn.piesat.framework.file.s3.config;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.file.s3.enums.S3ResponseEnum;
import cn.piesat.framework.file.s3.properties.OssProperties;

import cn.piesat.framework.file.s3.service.OssService;
import cn.piesat.framework.file.s3.service.impl.S3OssService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p/>
 * {@code @description}  :通用OSS服务
 * <p/>
 * <b>@create:</b> 2022/12/13 9:50.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(S3OssService.class)
    public OssService ossService(AmazonS3 amazonS3, OssProperties ossProperties){
        return new S3OssService(amazonS3,ossProperties);
    }

    @Bean
    @ConditionalOnMissingBean(AmazonS3.class)
    public AmazonS3 amazonS3(OssProperties ossProperties){
        final long count = Stream.builder()
                .add(ossProperties.getEndpoint())
                .add(ossProperties.getAccessSecret())
                .add(ossProperties.getAccessKey())
                .build()
                .filter(Objects::isNull)
                .count();
        if (count > 0) {
            throw new BaseException(S3ResponseEnum.OSS_SET_ERROR);
        }
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(ossProperties.getAccessKey(), ossProperties.getAccessSecret());
        final AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);
        return AmazonS3Client.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ossProperties.getEndpoint(),ossProperties.getRegion()))
                .withCredentials(awsStaticCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(ossProperties.isPathStyleAccess())
                .build();
    }
}

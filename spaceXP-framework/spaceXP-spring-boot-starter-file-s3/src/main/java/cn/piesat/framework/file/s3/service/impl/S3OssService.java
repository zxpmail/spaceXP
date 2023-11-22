package cn.piesat.framework.file.s3.service.impl;


import cn.piesat.framework.file.s3.properties.OssProperties;
import cn.piesat.framework.file.s3.service.OssService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :亚马逊s3协议服务
 * <p/>
 * <b>@create:</b> 2022/12/13 9:45.
 *
 * @author zhouxp
 */
@RequiredArgsConstructor
public class S3OssService implements OssService {
    private final AmazonS3 amazonS3;
    private final OssProperties ossProperties;
    @SneakyThrows
    @Override
    public void createBucket(String bucketName) {
        if ( !amazonS3.doesBucketExistV2(bucketName) ) {
            amazonS3.createBucket((bucketName));
        }
    }
    @SneakyThrows
    @Override
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }
    @SneakyThrows
    @Override
    public void bucketPolicy(String bucketName, String policy) {
        amazonS3.setBucketPolicy(bucketName, policy);
    }

    @SneakyThrows
    @Override
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    @SneakyThrows
    @Override
    public String getObjectURL(String bucketName, String objectName) {
        return amazonS3.getUrl(bucketName, objectName).toString();
    }

    @Override
    public String getObjectURL(String objectName) {
        return getObjectURL(ossProperties.getBucketName(),objectName);
    }

    @SneakyThrows
    @Override
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    @SneakyThrows
    @Override
    public void removeObject(String bucketName, String objectName)  {
        amazonS3.deleteObject(bucketName, objectName);
    }

    @SneakyThrows
    @Override
    public void removeObject(String objectName) {
        removeObject(ossProperties.getBucketName(),objectName);
    }

    @Override
    public S3Object getObject(String objectName) {
        return amazonS3.getObject(ossProperties.getBucketName(), objectName);
    }
    @SneakyThrows
    @Override
    public PutObjectResult putObject(String key, File file) {
        return amazonS3.putObject(ossProperties.getBucketName(), key, file);
    }
    @SneakyThrows
    @Override
    public void putObject(String bucketName, String objectName, InputStream stream, String contextType)  {
        putObject(bucketName, objectName, stream, stream.available(), contextType);
    }

    @SneakyThrows
    @Override
    public void putObject(String objectName, InputStream stream, String contextType)  {
        putObject(ossProperties.getBucketName(),objectName, stream, contextType);
    }

    @SneakyThrows
    @Override
    public void putObject(String objectName, InputStream stream) {
        putObject(ossProperties.getBucketName(),objectName, stream, "application/octet-stream");
    }

    @SneakyThrows
    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream inputStream, long size, String contentType)  {
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(size);
        metaData.setContentType(contentType);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream, metaData);
        putObjectRequest.getRequestClientOptions().setReadLimit((int) (size + 1));
        return amazonS3.putObject(putObjectRequest);
    }

    @Override
    public PutObjectResult putObject(String objectName, InputStream inputStream, long size, String contentType)  {
        return putObject(ossProperties.getBucketName(),objectName,inputStream,size,contentType);
    }

    @Override
    public ObjectListing listObjects(String bucketName) {
        return amazonS3.listObjects(bucketName);
    }



    @Override
    public AmazonS3 getS3Client() {
        return amazonS3;
    }
    @SneakyThrows
    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return objectListing.getObjectSummaries();
    }

    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String prefix, boolean recursive) {
        return getAllObjectsByPrefix(ossProperties.getBucketName(),prefix,recursive);
    }
}

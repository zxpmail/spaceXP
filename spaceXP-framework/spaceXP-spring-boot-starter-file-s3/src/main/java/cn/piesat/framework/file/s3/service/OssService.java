package cn.piesat.framework.file.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :OSS客户端
 * <p/>
 * <b>@create:</b> 2022/12/13 9:39.
 *
 * @author zhouxp
 */
public interface OssService {
    /**
     * 创建bucket
     * @param bucketName bucket名称
     */
    void createBucket(String bucketName);

    /**
     * 通过bucket名称删除bucket
     * @param bucketName bucket名称
     */
    void removeBucket(String bucketName);

    /**
     * 创建规则
     * @param bucketName bucket名称
     * @param policy 规则
     */
    void bucketPolicy(String bucketName, String policy);

    /**
     * 获取所有的bucket
     * @return
     */
    List<Bucket> getAllBuckets();

    /**
     * 获取对象的url
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return
     */
    String getObjectURL(String bucketName, String objectName);

    String getObjectURL(String objectName);
    /**
     * 获取文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 返回对象信息
     */
    S3Object getObject(String bucketName, String objectName);
    S3Object getObject(String objectName);

    /**
     * 通过bucketName和objectName删除对象
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @throws Exception
     */
    void removeObject(String bucketName, String objectName) throws Exception;


    void removeObject(String objectName);


    /**
     *
     * @param key
     * @param file
     * @return
     */
    PutObjectResult putObject(String key, File file);

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @param contextType 文件类型
     * @throws Exception
     */
    void putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception;

    void putObject(String objectName, InputStream stream, String contextType) throws Exception;

    void putObject(String objectName, InputStream stream) throws Exception;
    /**
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param inputStream 文件流
     * @param size 大小
     * @param contentType
     * @return
     * @throws IOException
     */
    PutObjectResult putObject(String bucketName, String objectName, InputStream inputStream, long size, String contentType) throws IOException;

    PutObjectResult putObject(String objectName, InputStream inputStream, long size, String contentType) throws IOException;

    ObjectListing listObjects(String bucketName) ;



    AmazonS3 getS3Client();

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param inputStream 文件流
     * @throws Exception
     */
    default PutObjectResult putObject(String bucketName, String objectName, InputStream inputStream) throws IOException {
        return putObject(bucketName, objectName, inputStream, inputStream.available(), "application/octet-stream");
    }

    /**
     * 根据文件前置查询文件
     * @param bucketName bucket名称
     * @param prefix 前缀
     * @param recursive 是否递归查询
     * @return S3ObjectSummary 列表
     */
    List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);

    List<S3ObjectSummary> getAllObjectsByPrefix(String prefix, boolean recursive);
}

# spaceXP

#### 介绍
基于 S3 协议的文件客户端，实现 MinIO、阿里云、腾讯云、七牛云、华为云等云服务

#### 软件架构
基于 S3 协议的文件客户端，实现 MinIO、阿里云、腾讯云、七牛云、华为云等云服务

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat</groupId>
        <artifactId>spaceXP-spring-boot-starter-file-s3</artifactId>
        <version>2.0.0</version>
    </dependency>
在项目的配置文件中加入
space.oss.accessKey  访问key
space.oss.accessSecret 访问密钥
space.oss.endpoint 如果是服务器MinIO等直接使用 [$schema]://[$ip]:[$port]
外网[$Schema]://[$Bucket].[$Endpoint]/[$Object]*
https://help.aliyun.com/document_detail/375241.html*
space.oss.region 区refres to com.amazonaws.regions.Regions*
https://help.aliyun.com/document_detail/31837.htm?spm=a2c4g.11186623.0.0.695178eb0nD6jp*
space.oss.path-style-access 
space.oss.bucketName 桶名称

    在Service中直接注入
    OssService ossService 即可对文件处理  


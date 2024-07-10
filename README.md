# spaceXP

#### 介绍
积木组件:积沙成塔，结合springboot如：url权限、数据权限、文件上传、数据访问等等组件进行分离、包装等可以独立引用
分为：2.0.0版本和3.0.0版本
2.0.0基于springboot2.x版本 开发基于springboot2.7.4 java8
3.0.0基于springboot3.x版本 开发基于springboot3.2.2 java17
#### 软件架构
spaceXP-dependencies 架构依赖组件
spaceXP-framework 核心组件集合：包含url权限、数据权限、文件上传、数据访问等等组件
spaceXP-tests 核心组件组件测试用例
spaceXP-tools 工具集合

#### 安装教程

1.  核心组件直接打包发布到私服上，项目就可以直接引用
2.  xxxx
3.  xxxx

#### 使用说明
## 框架说明
1.  spaceXP-common 公共功能：包含数据库AddGroup、UpdateGroup增加和更新注解；登录LoginUser注解和不进行自动包装返回值NoApiResult注解
    项目基本异常类BaseException类、基本响应接口和树形数据接口，ApiResult包装类分页PageResult类
    日志类以及实体复制工具CopyBeanUtils、生成树工具、jwt工具、servlet工具类
2.  spaceXP-spring-boot-starter-web包括：统一处理返回值、request域绑定登录信息、统计bean初始化时间、统一异常处理
3.  spaceXP-spring-boot-starter-mybatis-plus包含：自动填入字段、动态表名
4.  spaceXP-spring-boot-starter-knife4j knife4j包装工具组件
5.  spaceXP-spring-boot-starter-security 包括数据拦截脱敏、数据加解密
6.  spaceXP-spring-boot-starter-redis 包装redisTemplate常用功能
7.  spaceXP-spring-boot-starter-file-s3 基于 S3 协议的文件客户端，实现 MinIO、阿里云、腾讯云、七牛云、华为云等云服务
8.  spaceXP-spring-boot-starter-log 根据aop切面实现日志记录组件
9.  spaceXP-spring-boot-starter-feign feign包装组件，自动解包ApiResult以及支持OkHttp和HttpClient
10. spaceXP-spring-boot-starter-permission-url url权限拦截
11. spaceXP-spring-boot-starter-permission-data 数据权限
12. spaceXP-spring-boot-starter-multi-tenant 多租户组件
13. spaceXP-spring-boot-starter-dynamic-datasource 动态增加数据源
14. spaceXP-spring-boot-starter-kafka-datasource 动态增加kafka数据源
15. spaceXP-spring-boot-starter-mybatis-plus-external mybatis-plus额外组件 支持发号功能
16. spaceXP-spring-boot-starter-redis-external 包装redisson 分布式锁功能
17. spaceXP-spring-boot-starter-websocket 实现socket消息发送以及心跳功能
18. spaceXP-spring-boot-starter-log-external 实现log组件接口 直接保存到日志服务器中
## 工具说明
1. spaceXP-gateway 网关工具 实现网关鉴权
2. spaceXP-generator 低代码工具
3. spaceXP-generator-ui 低代码工具前端
4. spaceXP-log 日志服务器，日志工具 实现记录登录日志和普通日志到数据库中
## 测试说明
1. spaceXP-spring-boot-tests-dynamic-datasource测试动态数据源
2. spaceXP-spring-boot-tests-feign-producer 测试feign工具生产者
3. spaceXP-spring-boot-tests-feign-consumer 测试feign工具消费者
4. spaceXP-spring-boot-tests-file-s3 测试s3组件
5. spaceXP-spring-boot-tests-kafka-datasource 测试kafka数据源组件
6. spaceXP-spring-boot-tests-log 测试日志组件
7. spaceXP-spring-boot-tests-multi-tenant 测试多租户组件
8. spaceXP-spring-boot-tests-mybatis-plus 测试mybatis-plus组件
9. spaceXP-spring-boot-tests-permission-data测试数据权限
10. spaceXP-spring-boot-tests-permission-url 测试菜单权限
11. spaceXP-spring-boot-tests-redis 测试redis组件
12. spaceXP-spring-boot-tests-web 测试web组件
13. spaceXP-spring-boot-tests-websocket 测试websocket组件
14. spaceXP-spring-boot-tests-log-external 测试log扩展组件
#### 鸣谢
本软件从一些软件借鉴而来如：renren、ruoyi、yudao、pig、guns、jeecg、maku等等软件和作者
非常感谢众多软件和作者





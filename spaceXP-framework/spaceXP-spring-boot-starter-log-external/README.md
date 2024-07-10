# spaceXP

#### 介绍
基于springAOP实现对含有OpLog或者ApiOperation注解的方法进行日志记录

#### 软件架构
实现了log组件的ExecuteLogService直接使用

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-log-external</artifactId>
        <version>2.0.0</version>
    </dependency>
在项目的配置文件中加入
space.log.external.restUrlPrefix 不需要网关的rest 调用地址 有网关时不配置
space.log.external.logServerName 日志服务名 有网关必须配置，即注册中心的服务名称
space.log.external.save 调用日志服务器的请求日志http保存地址
space.log.external.restTemplateEnabled  是否启用restTemplate模式
使用该组件必须实现ExecuteLogService接口

一定在启动类上加
@EnableFeignClients(basePackages = {"cn.piesat.framework.log.external.client"})

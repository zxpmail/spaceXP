# spaceXP

#### 介绍
基于springAOP实现对含有OpLog或者ApiOperation注解的方法进行日志记录

#### 软件架构
1、基于springAOP实现对含有OpLog或者ApiOperation注解的方法进行日志记录
2、基于mdc生成文件分类方式记录日志文件
#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-log</artifactId>
        <version>2.0.0</version>
    </dependency>
在项目的配置文件中加入
space.log.log-flag=1记录方法含有OpLog注解 2含有ApiOperation注解的方法
space.log.headers 把含有headers列表中的key在request域中的值写入到日志中
使用该组件必须实现ExecuteLogService接口
    
    void exec(OpLogEntity opLogEntity)是用户应该写入日志的方法

mdc可以生成 unknown、other、business、app、audit、exception类别文件，配置见spaceXP-spring-boot-tests-log下的
logback-spring.xml文件
可以灵活配置生成文件格式
logging.file.path=d:/logs 生成日志路径
space.log.mdc.enable= true 开启mdc日志监控
space.log.mdc.appInfo app提示，可以默认
space.log.mdc.errorInfo error提示，可以默认
space.log.mdc.logType 日志类型，可以默认
space.log.mdc.logTypeCode 日志类型代码，可以默认
space.log.mdc.pointcutExpression 切面表达式，可以默认 @annotation(cn.piesat.framework.log.annotation.MdcLog)
space.log.mdc.threadAliveSeconds mdc线程存活 默认60秒
space.log.mdc.threadPoolQueueCapacity mdc线程池队列数 默认500
使用异步记录时 @Async("mdcExecutor")
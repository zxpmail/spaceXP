# spaceXP

#### 介绍
基于springAOP实现对含有OpLog或者ApiOperation注解的方法进行日志记录

#### 软件架构
基于springAOP实现对含有OpLog或者ApiOperation注解的方法进行日志记录

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


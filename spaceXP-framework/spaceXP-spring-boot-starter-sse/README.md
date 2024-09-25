# spaceXP

#### 介绍
实现sse消息发送以及心跳功能

#### 软件架构


#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-sse</artifactId>
        <version>2.0.0</version>
    </dependency>
    配置说明
    space.sse.timeout  # 超时时间 单位毫秒

    space.sse.reconnectTimeMillis #重连时间 单位毫秒

    space.sse.poolSize #设置线程池大小

    space.sse.heartbeatInterval #心跳间隔 单位毫秒

    space.sse.heartbeatMessage #心跳消息

具体参照spaceXP-spring-boot-test-sse
# spaceXP

#### 介绍
实现socket消息发送以及心跳功能

#### 软件架构


#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-webscoket</artifactId>
        <version>2.0.0</version>
    </dependency>

    配置说明
    space.websocket.debug  # 默认值为true，即调试模式 单用户 不进行登录验证

    space.websocket.handlerPath #处理路径

    space.websocket.allowedOrigin #是否允许跨域 默认false;

    space.websocket.domainNames #允许跨域的域名 默认 所有


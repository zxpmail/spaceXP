# spaceXP

#### 介绍
包装redisTemplate常用功能并增加redis常用配置类

#### 软件架构
包装redisTemplate常用功能并增加redis常用配置类

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-redis</artifactId>
        <version>2.0.0</version>
    </dependency>

    在Service中直接注入
    RedisService redisService即可 

发布订阅消息
在项目的配置文件中加入
space.redis.message-enable=true 开启发布订阅消息模式，默认不开启
space.redis.topics TOPIC名称
space.redis.prevent-replay-enable: true 开启防止重刷功能
space.redis.access-limit-enable: true 开启限流功能


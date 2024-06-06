# spaceXP

#### 介绍
redis 额外功能 增加 redisson 分布式锁功能

#### 软件架构


#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-redis-external</artifactId>
        <version>2.0.0</version>
    </dependency>

在controller 增加 @DLock("'person:' + #person.id + ':' + #cateId")参考spaceXP-spring-boot-tests-redis
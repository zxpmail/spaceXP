# spaceXP

#### 介绍
动态kafka组件

#### 软件架构
kafka组件

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-kafka</artifactId>
        <version>2.0.0</version>
    </dependency>

在项目的配置文件中加入
增加加解密拦截器 可以配置忽略加解密topic 读配置文件比较麻烦再系统变量中设置 System.setProperty(KafkaConstant.IGNORE_TOPICS, "my*");变量以逗号分隔
配置加解密拦截器：
spring.kafka.producer.properties.interceptor.classes=cn.piesat.framework.kafka.interceptor.EncryptionProducerInterceptor
spring.kafka.consumer.properties.interceptor.classes=cn.piesat.framework.kafka.interceptor.DecryptionConsumerInterceptor

例子参见spaceXP-spring-boot-tests-kafka




# spaceXP

#### 介绍
测试kafka数据源组件

#### 软件架构
测试kafka数据源组件

#### 使用说明
配置数据源如：

space:
    kafka:
        servers:
            server1:
                consumer: #kafka消费端核心配置
                    "[bootstrap.servers]": 192.168.2.114:9092
                    "[auto.commit.interval.ms]": 1000
                consumerExtra: #消费端工厂配置
                    consumer10:  # 消费者实例化到spring中的beanName
                        ackDiscarded: true
                        pollTimeout: 4000
                        factoryFilter:
                            serializerClass: cn.piesat.kafka.datasource.model.TestDTO
                            filter:
                                name: zhangSan,liSi
                                msg: "hello world"
                    consumer11:  # 消费者实例化到spring中的beanName
                        ackDiscarded: true
                        pollTimeout: 4000
                        factoryFilter:
                            strategy: cn.piesat.kafka.datasource.strategy.MyStrategy
                producer:
                    "[bootstrap.servers]": 192.168.2.114:9092
                    "[retries]": 2
                producerBeanName: producer10  #生产者实例化到spring中的beanName
        server2:
            producer:
                "[bootstrap.servers]": 192.168.2.114:9092
                "[retries]": 3
            producerBeanName: producer20  #生产者实例化到spring中的beanName
                consumer: #kafka消费端核心配置
                    "[bootstrap.servers]": 192.168.2.114:9092
                    "[auto.commit.interval.ms]": 1000
                consumerExtra: #消费端工厂配置
                    consumer20:  # 消费者实例化到spring中的beanName
                        ackDiscarded: true
                        pollTimeout: 4000
注意：在消费端和生产类上要加@DependsOn("kafkaCreator")
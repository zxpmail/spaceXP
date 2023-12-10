# spaceXP

#### 介绍
测试数据源组件

#### 软件架构
测试数据源组件

#### 使用说明
配置数据源如：

    spring:
        datasource:
        driverClassName: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/test1 #一定为jdbc-url不能为url
        username: root
        password: 123456
    test1数据库中有表
        db_info 通过DataSourceRunner动态从数据表db_info加载数据源
    可以用@DS("user_test")表示查询user_test数据源
    可以在方法上增加@DS和DSEntity参数进行动态加载数据源
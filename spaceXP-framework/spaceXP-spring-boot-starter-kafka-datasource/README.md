# spaceXP

#### 介绍
动态数据源组件：动态增加数据源组件

#### 软件架构
动态数据源组件：动态增加数据源组件

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-dynamic-datasource</artifactId>
        <version>2.0.0</version>
    </dependency>

在项目的配置文件中加入
space.datasource 为Map<String,DataSourceEntity> 类型配置数据源map，自动增加数据源用@DS进行注解分不同数据源
例子参见spaceXP-spring-boot-tests-dynamic-datasource





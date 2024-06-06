# spaceXP

#### 介绍
mybatis-plus进行一些包装：包括动态表名、自动填写功能、日期转换

#### 软件架构
mybatis-plus进行一些包装

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-mybatis-plus-external</artifactId>
        <version>2.0.0</version>
    </dependency>

在项目的配置文件中加入
space.db.external.keyPrefix key前缀
space.db.external.length 生成序列长度范围
space.db.external.workId 工作区id
注意所以id字段java实体类为LONG类型  @TableId(type = IdType.ASSIGN_ID)







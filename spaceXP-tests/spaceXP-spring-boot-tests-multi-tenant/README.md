# spaceXP

#### 介绍
测试多租户组件

#### 软件架构
测试多租户组件

#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-multi-tenant</artifactId>
        <version>2.0.0</version>
    </dependency>
在项目的配置文件中加入
在请求时header加入tenantId和userName字段 否则不进行多租户
space.tenant.tenant-id-column 默认值tenant_id 数据库中多租标志字段
space.tenant.ignore-tables 在进行数据库操作时增加tenant_id条件的表名列表(没有多租户id的表一般需要)
space.tenant.ignore-sql  配置不进行多租户隔离的sql 需要配置mapper的全路径如：cn.hello.hello.dao.mapper.UserMapper.selectList
space.tenant.ignore-user 配置不进行多租户隔离的用户
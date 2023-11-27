# spaceXP

#### 介绍
url权限拦截

#### 软件架构
url权限拦截

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-permission-url</artifactId>
        <version>2.0.0</version>
    </dependency>

在项目的配置文件中加入
space.permission.url.whiteList url白名单列表
space.permission.url.userList 用户白名单列表 从header域中取值对比
space.permission.url.url-patterns 过滤路径 默认值：/*
space.permission.url.url-permission-filter 权限名称 默认值：urlPermissionFilter

加入项目必须实现UserUrlPermissionService接口
如果该用户是白名单中的用户不进行url拦截处理，否则要
取得用户的权限url集合和访问url进行对比处理，含有允许访问该url 否则不允许访问该url







# spaceXP

#### 介绍
数据权限组件：根据用户、部门进行数据权限拦截

#### 软件架构
mybatis-plus的数据权限接口进行实现

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-permission-data</artifactId>
        <version>2.0.0</version>
    </dependency>

在项目的配置文件中加入
space.permission.data.ignore-users 忽略用户名即不对含有此用户名进行数据权限拦截
space.permission.data.ignore-conditions 忽略mapper或者mapper中的方法
space.permission.data.creator-id-column-name 创建数据的用户id 默认值creator
space.permission.data.deptIdColumnName 默认值为：dept_id，创建数据的用户所在部门的id或子部门id

space.permission.data.ignore-conditions[0]=UserMapper.selectById
对UserMapper.selectById就不进行数据权限拦截
space.permission.data.ignore-conditions[0]=admin
当header域或request域中根据userId调用项目实现类UserDataPermissionService中的
getDataPermission方法中得到的实体UserDataPermission中的username与admin一致时候就进行忽略admin用户
在不设置配置文件就根据UserDataPermission中的权限类别进行拦截
1、自身权限即：根据用户creator字段进行拦截
2、部门权限：根据用户的部门id进行拦截
3、部门及以下数据权限：根据用户的部门id以及子部门ID进行拦截
4、全部数据权限：即不拦截用户数据权限







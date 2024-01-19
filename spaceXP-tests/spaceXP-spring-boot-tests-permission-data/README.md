# spaceXP

#### 介绍
测试数据权限

#### 软件架构
测试数据权限

#### 使用说明
space.permission.data.conditions[0]=UserMapper 只对UserMapper进行数据权限拦截
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
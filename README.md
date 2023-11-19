# spaceXP

#### 介绍
积木组件:积沙成塔，结合springboot如：url权限、数据权限、文件上传、数据访问等等组件进行分离、包装等可以独立引用

#### 软件架构
spaceXP-dependencies 架构依赖组件
spaceXP-framework 核心组件集合：包含url权限、数据权限、文件上传、数据访问等等组件
1、spaceXP-common 公共功能
spaceXP-tests 核心组件组件测试用例
spaceXP-tools 工具集合

#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  spaceXP-common 公共功能：包含数据库AddGroup、UpdateGroup增加和更新注解；登录LoginUser注解和不进行自动包装返回值NoApiResult注解
    项目基本异常类BaseException类、基本响应接口和树形数据接口，ApiResult包装类分页PageResult类
    日志类以及实体复制工具CopyBeanUtils、生成树工具、jwt工具、servlet工具类
2.  spaceXP-spring-boot-starter-web包括：统一处理返回值、request域绑定登录信息、统计bean初始化时间、统一异常处理
3.  spaceXP-spring-boot-starter-mybatis-plus包含：自动填入字段、动态表名
4.  spaceXP-spring-boot-starter-knife4j knife4j包装工具组件

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


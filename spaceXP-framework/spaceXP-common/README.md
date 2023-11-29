# spaceXP

#### 介绍
公共功能：包含数据库AddGroup、UpdateGroup增加和更新注解；登录LoginUser注解和不进行自动包装返回值NoApiResult注解
项目基本异常类BaseException类、基本响应接口和树形数据接口，ApiResult包装类分页PageResult类
日志类以及实体复制工具CopyBeanUtils、生成树工具、jwt工具、servlet工具类

#### 软件架构
公共功能：包含数据库AddGroup、UpdateGroup增加和更新注解；登录LoginUser注解和不进行自动包装返回值NoApiResult注解
项目基本异常类BaseException类、基本响应接口和树形数据接口，ApiResult包装类分页PageResult类
日志类以及实体复制工具CopyBeanUtils、生成树工具、jwt工具、servlet工具类

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-common</artifactId>
        <version>2.0.0</version>
    </dependency>
项目配置
space.common.result.message 默认值message 可以修改自动打包api请求json的属性
space.common.result.code 默认值code 可以修改自动打包api请求json的属性
space.common.result.data 默认值data 可以修改自动打包api请求json的属性

space.common.response-code.success-code 默认值200 可以定义成功返回码值
space.common.response-code.success-value 默认值操作成功 可以定义成功返回值
space.common.response-code.error-code 默认值500 可以定义失败返回码值
space.common.response-code.error-value 默认值操作失败 可以定义失败返回值
等等返回code和值



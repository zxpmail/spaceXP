# spaceXP

#### 介绍
统一处理返回值、request域绑定登录信息、统计bean初始化时间、统一异常处理

#### 软件架构
处理并包装web统一返回及异常等

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-web</artifactId>
        <version>2.0.0</version>
    </dependency>

    controller层方法就可以直接写函数原始值，不要特意的包装例如：
    @GetMapping("returnValue")
    public String returnValue(){
        return "returnValue";
    }
    
    返回
    {
        "code": 200,
        "message": "操作成功",
        "data": "returnValue"
    }
    
    如果自己已经包装返回值就可以在类上或者方法上增加@NoApiResult就不进行二次包装

在项目的配置文件中加入
space.web.cost-enable=true 开启bean初始化花费统计时间，默认不开启
space.web.web-exception-enable=true 默认开启 是否使用统一异常
space.web.return-value-enable 默认开启 是否开启统一包装返回值
space.web.login-user-enable 默认开启  是否开启用户登录信息绑定到request域
space.web.date-formatter-enable 默认不开启 是否开启LocalDatetime web自动转换
space.web.date-time-pattern 默认格式：yyyy-MM-dd HH:mm:ss 设置转换格式
space.web.ignore-urls 忽略统一包装的url



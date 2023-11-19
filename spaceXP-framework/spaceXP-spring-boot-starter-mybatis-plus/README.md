# spaceXP

#### 介绍
mybatis-plus进行一些包装：包括动态表名、自动填写功能、日期转换

#### 软件架构
mybatis-plus进行一些包装

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat</groupId>
        <artifactId>spaceXP-spring-boot-starter-mybatis-plus</artifactId>
        <version>2.0.0</version>
    </dependency>

在项目的配置文件中加入
space.db.date-format-enable日期格式转换，只支持LocalDateTime类型，默认不开启，开启等于true
space.db.date_format 设置时间格式 默认为：yyyy-MM-dd HH:mm:ss
space.db.db-type 设置项目连接数据库类型，默认为mysql设置和mybaits-plus相同
space.db.tenant_id 默认值为：tenant_id设置多租户id，按实际数据库字段设置
space.db.dept_id 默认值为：dept_id 设置数据创建者所属部门ID字段，按实际数据库字段设置
space.db.update_id 默认值为：update_id 表数据更新者ID字段，按实际数据库字段设置
space.db.create_id 默认值为：create_id 表数据创建者ID字段，按实际数据库字段设置
space.db.update_time 默认值为：update_time 表数据更新时间字段，按实际数据库字段设置
space.db.create_time 默认值为：create_time 表数据创建时间字段，按实际数据库字段设置
注意所以id字段java实体类为LONG类型 时间为LocateDateTime类型生效
要想自动填充生效必须在实体类的要自动填充字段上增加@TableField(fill = FieldFill.INSERT)或 @TableField(fill = FieldFill.INSERT_UPDATE)mybatis-plus注解
space.table.enable 动态表名是否开启，默认不开启
space.table.table-prefix可以增加要处理的表名
使用动态表名时必须在controller方法增加@DynamicTableName注解并且第一参数是动态表名的名称







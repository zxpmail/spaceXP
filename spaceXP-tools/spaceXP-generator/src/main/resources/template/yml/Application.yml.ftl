server:
    port: ${port?c}
    servlet:
        context-path:  /${moduleName}
spring:
    application:
        name: ${moduleName}
    mvc:
        path-match:
            matching-strategy: ANT_PATH_MATCHER
    datasource:
        driverClassName: ${driverClassName}
        url: ${url}
        username: ${username}
        password: ${password}
    jackson:
        time-zone: GMT+8
        date-format: yyyy-MM-dd HH:mm:ss

mybatis-plus:
    mapperLocations: classpath:mapper/**/*.xml
    configuration:
        log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    global-config:
        db-config:
            logic-delete-field: del_flag # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
            logic-delete-value: 1 # 逻辑已删除值(默认为 1)
            logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

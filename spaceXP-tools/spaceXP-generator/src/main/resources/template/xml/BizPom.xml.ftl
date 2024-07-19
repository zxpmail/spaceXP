<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>${package}</groupId>
    <artifactId>${moduleName}-biz</artifactId>
    <version>${version}</version>
    <name>${moduleName}-biz</name>
    <description>${description}</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <#if dbType=="MySQL">
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.2.0</version>
        </dependency>
        </#if>

        <#if dbType=='PostgreSQL'>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.6.0</version>
        </dependency>
        </#if>
        <#if dbType=='DM'>
        <dependency>
            <groupId>com.dameng</groupId>
            <artifactId>DmJdbcDriver18</artifactId>
            <version>8.1.2.141</version>
        </dependency>
        </#if>
        <dependency>
            <groupId>cn.piesat.space</groupId>
            <artifactId>spaceXP-spring-boot-starter-web</artifactId>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>cn.piesat.space</groupId>
            <artifactId>spaceXP-spring-boot-starter-mybatis-plus</artifactId>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>cn.piesat.space</groupId>
            <artifactId>spaceXP-spring-boot-starter-knife4j</artifactId>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>${package}</groupId>
            <artifactId>${moduleName}-model</artifactId>
            <version>${version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.7.18</version>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
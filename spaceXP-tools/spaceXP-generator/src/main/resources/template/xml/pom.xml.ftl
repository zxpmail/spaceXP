<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.4</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>${package}</groupId>
    <artifactId>${moduleName}</artifactId>
    <version>${version}</version>
    <name>${moduleName}</name>
    <description>${description}</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
    <#if dbType=="mysql">
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
    </#if>

    <#if dbType=="postgresql">
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
    </#if>
    <#if dbType=="dm">
        <dependency>
            <groupId>com.dameng</groupId>
            <artifactId>DmJdbcDriver18</artifactId>
            <version>8.1.2.141</version>
        </dependency>
    </#if>
    <#if projectType==2 >
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </#if>

    <#if projectType ==3 ||  projectType ==4 >
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            <version>2021.0.4.0</version>
        </dependency>
    </#if>

    <#if projectType ==4 >
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            <version>2021.0.4.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
            <version>3.1.5</version>
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
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <#if projectType ==2 >
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>2021.0.4</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
        </dependencyManagement>
    </#if>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.7.4</version>
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

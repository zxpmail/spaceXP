# spaceXP

#### 介绍
安全组件包括数据拦截脱敏、数据加解密

#### 软件架构
安全组件包括数据拦截脱敏、数据加解密

#### 安装教程
直接生成jar包
#### 使用说明
    <dependency>
        <groupId>cn.piesat.space</groupId>
        <artifactId>spaceXP-spring-boot-starter-security</artifactId>
        <version>2.0.0</version>
    </dependency>

在项目的配置文件中加入
space.security.enable=true 开启bean加解密AOP拦截，默认不开启
space.security.secretKey 加解密秘钥

        @Data
        public class DesensitizeDO {
        // 姓名
        @Desensitize(rule = DesensitizeRuleEnums.CHINESE_NAME)
        private String name; 
        // 邮箱
        @Desensitize(rule = DesensitizeRuleEnums.EMAIL)
        private String email;
        
        // 电话
        @Desensitize(rule = DesensitizeRuleEnums.MOBILE_PHONE)
        private String phone;
    
        @CustomDesensitize(start = 1,end = 2)
        private String custom;
    }
    实体类在controller返回层进行脱敏

加解密：

    @Data
    public class EncryptDO {
        @EncryptField
        private  String name;
        private Integer age;
    }
    
    @ApiOperation("测试加密")
    @PostMapping("encrypt")
    @EncryptMethod
    public EncryptDO encrypt(@RequestBody EncryptDO encryptDO) {
        return encryptDO;
    }

    @ApiOperation("测试解密")
    @PostMapping("decrypt")
    @DecryptMethod
    public EncryptDO decrypt() {
        EncryptDO encryptDO = new EncryptDO();
        encryptDO.setAge(12);
        encryptDO.setName("E7798C31C4A94F2C43DE78FE8D050D40");
        return encryptDO;
    }
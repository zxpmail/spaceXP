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

    使用开源的证书管理引擎TrueLicense
        1、生成密钥对，使用Keytool生成公私钥证书库
        2、授权者保留私钥，使用私钥和使用日期，生成证书license
        3、公钥与生成的证书给使用者（放在验证的代码中使用），验证证书license是否在有效期内
        #validity：私钥的有效期（单位：天）
        #alias：私钥别称
        #keystore:私钥库文件名称(生成在当前目录)
        #storepass：私钥库的密码(获取keystore信息所需的密码)
        #keypass：私钥的密码
        #dname证书个人信息
        #CN为你的姓名
        #OU为你的组织单位名称
        #O为你的组织名称
        #L为你所在的城市名称
        #ST为你所在的省份名称
        #C为你的国家名称或区号
        keytool -genkeypair -keysize 1024 -validity 3650 -alias privateKey -keystore privateKeys.keystore -storepass public_password1234 -keypass private_password1234 -dname CN=localhost,OU=localhost,O=localhost,L=SH,ST=SH,C=CN
        
        ##2.把私匙库内的公匙导出到一个文件当中
        #alias：私钥别称
        #keystore：私钥库的名称(在当前目录查找)
        #storepass:私钥库的密码
        #file：证书名称
        keytool -exportcert -alias "privateKey" -keystore "privateKeys.keystore" -storepass "public_password1234" -file "certfile.cer"
        
        ##3.再把这个证书文件导入到公匙库
        #alias：公钥别称
        #file：证书名称
        #keystore：公钥文件名称(生成在当前目录)
        #storepass：私钥库的密码
        keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.keystore" -storepass "public_password1234"  

        上述命令执行完成后会在当前目录生成三个文件：
        1、certfile.cer 认证证书文件，已经没用了，可以删除
        2、privateKeys.keystore 私钥文件，自己保存，以后用于生成license.lic证书
        3、publicKeys.keystore 公钥文件，以后会和license.lic证书一起放到使用者项目里
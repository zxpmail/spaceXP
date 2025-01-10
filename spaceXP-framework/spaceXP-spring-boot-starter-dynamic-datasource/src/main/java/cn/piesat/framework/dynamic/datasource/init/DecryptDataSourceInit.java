package cn.piesat.framework.dynamic.datasource.init;

import cn.piesat.framework.common.utils.RsaEncryptUtil;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import org.springframework.util.StringUtils;


/**
 * <p/>
 * {@code @description}: 解密数据源用户，密码等明文密文
 * <p/>
 * {@code @create}: 2025-01-09 13:34
 * {@code @author}: zhouxp
 */
public class DecryptDataSourceInit implements DataSourceInit {

    private String decrypt(String privateKey, String cipherText) {
        if (StringUtils.hasText(cipherText)) {
            return RsaEncryptUtil.rsaDecrypt(cipherText, privateKey);
        }
        return cipherText;
    }

    @Override
    public void beforeCreate(DataSourceEntity dataSourceEntity) {
        String privateKey = dataSourceEntity.getPrivateKey();
        if (StringUtils.hasText(privateKey)) {
            dataSourceEntity.setUsername(decrypt(privateKey, dataSourceEntity.getUsername()));
            dataSourceEntity.setPassword(decrypt(privateKey, dataSourceEntity.getPassword()));
        }
    }
}

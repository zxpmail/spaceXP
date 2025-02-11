package cn.piesat.framework.dynamic.datasource.init;

import cn.piesat.framework.common.utils.RsaUtils;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import org.springframework.util.StringUtils;


/**
 * <p/>
 * {@code @description}: 解密数据源用户，密码等明文密文
 * <p/>
 * {@code @create}: 2025-01-09 13:34
 * {@code @author}: zhouxp
 */
public class DecryptDataSourceInit implements DataSourceInit {

    @Override
    public void beforeCreate(DataSourceProperty dataSourceProperty) {
        String privateKey = dataSourceProperty.getPrivateKey();
        if (StringUtils.hasText(privateKey)) {
            dataSourceProperty.setUsername(decrypt(privateKey, dataSourceProperty.getUsername()));
            dataSourceProperty.setPassword(decrypt(privateKey, dataSourceProperty.getPassword()));
        }
    }

    @Override
    public void afterCreate(DataSourceProperty dataSourceProperty) {

    }

    private String decrypt(String privateKey, String cipherText) {
        if (StringUtils.hasText(cipherText)) {
            return RsaUtils.rsaDecrypt(cipherText, privateKey);
        }
        return cipherText;
    }
}

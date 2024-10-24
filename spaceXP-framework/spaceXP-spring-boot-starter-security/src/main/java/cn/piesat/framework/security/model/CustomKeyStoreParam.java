package cn.piesat.framework.security.model;

import de.schlichtherle.license.AbstractKeyStoreParam;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p/>
 * {@code @description}: 自定义KeyStoreParam
 * <p/>
 * {@code @create}: 2024-10-24 14:35
 * {@code @author}: zhouxp
 */
public class CustomKeyStoreParam extends AbstractKeyStoreParam {
    private final String storePath;
    private final String alias;
    private final String storePwd;
    private final String keyPwd;

    public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    @Override
    public InputStream getStream() throws IOException {
        return Files.newInputStream(Paths.get(storePath));
    }
}
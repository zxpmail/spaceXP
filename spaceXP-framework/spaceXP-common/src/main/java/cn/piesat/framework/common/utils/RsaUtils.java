package cn.piesat.framework.common.utils;

import cn.piesat.framework.common.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 加解密类
 * <p/>
 * {@code @create}: 2025-01-08 9:05
 * {@code @author}: zhouxp
 */
@Slf4j
public class RsaUtils {

    private RsaUtils() {
    }

    public static final String SIGN_TYPE_RSA = "RSA";

    /**
     * RSA最大加密密文大小
     */
    private static final int MAX_BLOCK = 256;

    private static final int DEFAULT_KEY_SIZE = 2048;


    public static Map<String, String> generateRSAKeyPairs() {
        return generateRSAKeyPairs(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成公私钥
     */
    public static Map<String, String> generateRSAKeyPairs(int keySize) {
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance(SIGN_TYPE_RSA);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate RSA key pairs", e);
        }
        keyGen.initialize(keySize);
        KeyPair keyPair = keyGen.genKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        Map<String, String> map = new HashMap<>();
        map.put(CommonConstants.PUBLIC_KEY, Base64.encodeBase64String(publicKey.getEncoded()));
        map.put(CommonConstants.PRIVATE_KEY, Base64.encodeBase64String(privateKey.getEncoded()));
        return map;
    }


    /**
     * 加密
     */
    public static String rsaEncrypt(String content, String publicKey) {
        return rsaEncrypt(content, publicKey, null);
    }

    public static String rsaEncrypt(String content, String publicKey, String charset) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PublicKey pubKey = getPublicKeyFromX509(new ByteArrayInputStream(publicKey.getBytes()));
            Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] data = StringUtils.hasText(charset) ? content.getBytes(charset) : content.getBytes();
            enDecrypt(out, cipher, data);
            byte[] encryptedData = encodeBase64(out.toByteArray());

            return StringUtils.hasText(charset) ? new String(encryptedData, charset) : new String(encryptedData);
        } catch (Exception e) {
            log.error("EncryptContent = {},charset = {} ", content, charset, e);
            throw new RuntimeException("EncryptContent = " + content + ",charset = " + charset, e);
        }
    }

    /**
     * 解密
     */
    public static String rsaDecrypt(String content, String privateKey) {
        return rsaDecrypt(content, privateKey, null);
    }

    public static String rsaDecrypt(String content, String privateKey, String charset) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PrivateKey priKey = getPrivateKeyFromPKCS8(new ByteArrayInputStream(privateKey.getBytes()));
            Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] data = StringUtils.hasText(charset) ? decodeBase64(content.getBytes(charset)) : decodeBase64(content.getBytes());
            enDecrypt(out, cipher, data);
            byte[] decryptedData = out.toByteArray();

            return StringUtils.hasText(charset) ? new String(decryptedData, charset) : new String(decryptedData);
        } catch (Exception e) {
            log.error("EncryptContent = {},charset = {} ", content, charset, e);
            throw new RuntimeException("EncryptContent = " + content + ",charset = " + charset, e);
        }
    }

    private static void enDecrypt(ByteArrayOutputStream out, Cipher cipher, byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        int inputLen = data.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_BLOCK;
        }
    }

    private static byte[] encodeBase64(final byte[] binaryData) {
        return Base64.encodeBase64(binaryData);
    }

    private static PublicKey getPublicKeyFromX509(InputStream ins) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(RsaUtils.SIGN_TYPE_RSA);

        byte[] encodedKey = IOUtils.toByteArray(ins);

        encodedKey = decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    private static PrivateKey getPrivateKeyFromPKCS8(InputStream ins) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(RsaUtils.SIGN_TYPE_RSA);

        byte[] encodedKey = IOUtils.toByteArray(ins);

        encodedKey = decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    private static byte[] decodeBase64(final byte[] base64Data) {
        return Base64.decodeBase64(base64Data);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Map<String, String> map = generateRSAKeyPairs();
        System.out.println("publicKey:  " + map.get(CommonConstants.PUBLIC_KEY));
        System.out.println("privateKey: " + map.get(CommonConstants.PRIVATE_KEY));
        String cipherText = rsaEncrypt("piesat", map.get(CommonConstants.PUBLIC_KEY));

        System.out.println("密文：" + cipherText);
        String plainText = rsaDecrypt(cipherText, map.get(CommonConstants.PRIVATE_KEY));
        System.out.println("解密之后：" + plainText);
    }
}

package cn.piesat.framework.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * <p/>
 * {@code @description}: Aes工具类
 * <p/>
 * {@code @create}: 2025-01-15 8:54
 * {@code @author}: zhouxp
 */
@Slf4j
public class AesUtils {
    private static final String AES = "AES";
    /**
     * 使用CBC模式和PKCS5填充
     */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    /**
     * 固定的 16 字节密钥
     */
    private static final String DEFAULT_KEY = "0123456789abcdef";
    /**
     * 固定的 16字节IV
     */
    private static final String DEFAULT_IV = "abcdefghijklmnop";

    /**
     * AES-128, for other key lengths adjust accordingly
     */
    private static final int KEY_LENGTH = 16;
    private static final int IV_LENGTH = 16;

    private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/=]*$");

    private static SecretKeySpec secretKey;

    private static IvParameterSpec ivSpec;


    public static synchronized void init(String key, String iv) {
        if (!StringUtils.hasText(key)){
            key = DEFAULT_KEY;
        }
        if (!StringUtils.hasText(iv)) {
            iv = DEFAULT_IV;
        }
        if (key.getBytes(StandardCharsets.UTF_8).length != KEY_LENGTH) {
            throw new RuntimeException("Invalid key length: key length must be " + KEY_LENGTH + " bytes");
        }
        if (iv.getBytes(StandardCharsets.UTF_8).length != IV_LENGTH) {
            throw new RuntimeException("Invalid IV length: iv length must be " + IV_LENGTH + " bytes");
        }
        try {
            secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
            ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密
     */
    public static String encrypt(String data) {
        if (!StringUtils.hasText(data)) {
            return data;
        }
        if (secretKey == null) {
            init(DEFAULT_KEY,DEFAULT_IV);
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("Unexpected encryption error", e);
            throw new RuntimeException("Unexpected encryption error", e);
        }
    }

    private static boolean isBase64Valid(String data) {
        if (!BASE64_PATTERN.matcher(data).matches()) {
            return false;
        }

        String trimmedInput = data.trim().replaceAll("=+$", "");
        int paddingCount = data.length() - trimmedInput.length();

        return paddingCount <= 2 && (trimmedInput.length() % 4 == 0 || paddingCount > 0);
    }

    /**
     * 解密
     */
    public static String decrypt(String encryptedData) {
        if (!StringUtils.hasText(encryptedData)) {
            return encryptedData;
        }
        if (secretKey == null) {
            init(DEFAULT_KEY,DEFAULT_IV);
        }
        if (!isBase64Valid(encryptedData)) {
            return encryptedData;
        }
        try {

            byte[] decode = Base64.getDecoder().decode(encryptedData);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] original = cipher.doFinal(decode);
            return new String(original);
        } catch (Exception e) {
            log.error("Decryption failed", e);
        }
        return encryptedData;
    }

    /**
     * 辅助方法：将字节数组转换为十六进制字符串表示
     */


    public static void main(String[] args) {

        init(DEFAULT_KEY,DEFAULT_IV);
        String originalData = "Hello, World! ";


        String encryptedData = AesUtils.encrypt(originalData);
        //System.out.println("Encrypted Data (as hex): " + bytesToHex(encryptedData));

        String decryptedData = AesUtils.decrypt(encryptedData);
        System.out.println(decryptedData);


    }
}

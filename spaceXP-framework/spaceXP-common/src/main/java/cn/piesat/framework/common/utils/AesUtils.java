package cn.piesat.framework.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * <p/>
 * {@code @description}: Aes工具类
 * <p/>
 * {@code @create}: 2025-01-15 8:54
 * {@code @author}: zhouxp
 */
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
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;


    public static void init() {
        init(DEFAULT_KEY, DEFAULT_IV);
    }

    public static synchronized void init(String key, String iv) {
        if (key == null || iv == null) {
            throw new IllegalArgumentException("Key and IV cannot be null");
        }
        if (key.getBytes(StandardCharsets.UTF_8).length != KEY_LENGTH) {
            throw new IllegalArgumentException("Invalid key length: must be " + KEY_LENGTH + " bytes");
        }
        if (iv.getBytes(StandardCharsets.UTF_8).length != IV_LENGTH) {
            throw new IllegalArgumentException("Invalid IV length: must be " + IV_LENGTH + " bytes");
        }
        try {
            encryptCipher = Cipher.getInstance(ALGORITHM);
            decryptCipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密
     */
    public static byte[] encrypt(String data) {
        if (encryptCipher == null) {
            init();
        }
        try {
            return encryptCipher.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /**
     * 解密
     */
    public static String decrypt(byte[] encryptedData) {
        if (decryptCipher == null) {
            init();
        }
        try {
            byte[] original = decryptCipher.doFinal(encryptedData);
            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    /**
     * 辅助方法：将字节数组转换为十六进制字符串表示
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "";
        }

        // 预定义的十六进制字符数组
        final char[] hexArray = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            // 将每个字节转换为两个十六进制字符
            int v = b & 0xFF;
            sb.append(hexArray[v >>> 4]);
            sb.append(hexArray[v & 0x0F]);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        System.out.println();
        for (int i = 0; i < 1000000; i++) {
            String originalData = "Hello, World! System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData Hello, World! System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData Hello, World! System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData Hello, World! System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData Hello, World! System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData Hello, World! System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData System.out.println(\"Original Data: \" + originalData";
            //System.out.println("Original Data: " + originalData);

            byte[] encryptedData = AesUtils.encrypt(originalData);
            //System.out.println("Encrypted Data (as hex): " + bytesToHex(encryptedData));

            String decryptedData = AesUtils.decrypt(encryptedData);
            //System.out.println("Decrypted Data: " + decryptedData);

//            if (originalData.equals(decryptedData)) {
//                System.out.println("Encryption and decryption were successful.");
//            } else {
//                System.out.println("Encryption and decryption failed.");
//            }
        }
        System.out.println(System.currentTimeMillis() - l);

    }
}

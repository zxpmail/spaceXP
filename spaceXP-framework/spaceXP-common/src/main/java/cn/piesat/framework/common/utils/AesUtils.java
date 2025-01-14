package cn.piesat.framework.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <p/>
 * {@code @description}: Aes加解密工具类
 * <p/>
 * {@code @create}: 2025-01-14 8:48
 * {@code @author}: zhouxp
 */
@Slf4j
public class AesUtils {
    private static final String KEY_AES = "AES";
    private static final int KEY_SIZE = 128;

    private static final String KEY = "123456";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static SecretKeySpec keySpec;

    public static synchronized void init(String key) {
        if (!StringUtils.hasText(key)) {
            log.error("key 不能为空");
            throw new RuntimeException("key 不能为空");
        }

        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator kGen = KeyGenerator.getInstance(KEY_AES);
            //2.根据encode Rules规则初始化密钥生成器
            SecureRandom random = SecureRandom.getInstanceStrong();
            random.setSeed(key.getBytes(DEFAULT_CHARSET));
            kGen.init(KEY_SIZE, random);
            //3.产生原始对称密钥
            SecretKey secretKey = kGen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] enCodeFormat = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("AES 密文处理异常", e);
            throw new RuntimeException("AES 密文处理异常", e);
        }
    }

    /**
     * 加解密
     *
     * @param data 待处理数据
     * @param mode 加解密mode
     */
    private static String doAES(String data, int mode) {
        if (!StringUtils.hasText(data)) {
            throw new IllegalArgumentException("data is null: ");
        }
        if (keySpec == null) {
            init(KEY);
        }
        // 验证 mode 参数
        if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE) {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        boolean encrypt = mode == Cipher.ENCRYPT_MODE;
        //6.根据指定算法AES自成密码器
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(KEY_AES);
            cipher.init(mode, keySpec);
            byte[] content;
            if (encrypt) {
                content = data.getBytes(DEFAULT_CHARSET);
            } else {
                content = parseHexStr2Byte(data);
            }
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                return parseByte2HexStr(result);
            } else {
                return new String(result, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将二进制转换成16进制
     */
    private static String parseByte2HexStr(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(buf.length * 2);
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr == null || hexStr.isEmpty()) {
            return new byte[0];
        }

        if (hexStr.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string length must be even");
        }

        for (char c : hexStr.toCharArray()) {
            if (!(Character.digit(c, 16) >= 0)) {
                throw new IllegalArgumentException("Invalid hex character: " + c);
            }
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Character.digit(hexStr.charAt(i * 2), 16);
            int low = Character.digit(hexStr.charAt(i * 2 + 1), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 加密
     *
     * @param data 需要加密的内容
     */
    public static String encrypt(String data) {
        return doAES(data, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     */
    public static String decrypt(String data) {
        return doAES(data, Cipher.DECRYPT_MODE);
    }

    public static void main(String[] args) {
        String encryptIdCard = encrypt("420101196207212033");
        System.out.println(encryptIdCard);
        String decryptIdCard = decrypt(encryptIdCard);
        System.out.println(decryptIdCard);

        encryptIdCard = encrypt("1142010119620721203311");
        System.out.println(encryptIdCard);
        decryptIdCard = decrypt(encryptIdCard);
        System.out.println(decryptIdCard);
    }
}

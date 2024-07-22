package cn.piesat.framework.redis.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p/>
 * {@code @description}: md5工具
 * <p/>
 * {@code @create}: 2024-07-22 17:25
 * {@code @author}: zhouxp
 */
public class Md5Util {
    private final static String MD5= "MD5";
    private final static String ZERO = "0";
    private final static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static String generateMD5(String input) {
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, ZERO);
            }
            return hashText.toString();
    }
}

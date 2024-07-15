package cn.piesat.framework.common.utils;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import com.google.gson.Gson;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.gson.io.GsonSerializer;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * @author :zhouxp
 * @date 2022/9/8 15:29
 * @description :
 */
@Slf4j
public class JwtUtils {

    private static final Gson gson =new Gson();
    private static SecretKey getSecretKey(String tokenSignKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSignKey));
    }
    public static String createToken(Object user,String tokenSignKey){
        return Jwts.builder()
                .claim(CommonConstants.USER,user)
                .signWith(getSecretKey(tokenSignKey))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(CommonConstants.SUBJECT)
                .compact();

    }
    public static String createToken(JwtUser jwtUser,String tokenSignKey,Long tokenExpiration ) {
        String token = Jwts.builder()
                .claim(CommonConstants.USER, jwtUser)
                .setId(UUID.randomUUID().toString())
                .setSubject(CommonConstants.SUBJECT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSecretKey(tokenSignKey))
                .serializeToJsonWith(new GsonSerializer<>(new Gson()))
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        log.info("token = " + token);
        return token;
    }

    public static Object getValue(String token,String tokenSignKey){
        if (!StringUtils.hasText(token)){
            throw new BaseException(CommonResponseEnum.TOKEN_EMPTY);
        }
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(tokenSignKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(CommonConstants.USER);
    }
    public static JwtUser checkToken(String token,String tokenSignKey) {
        String msg;
        if (!StringUtils.hasText(token)){
            throw new BaseException(CommonResponseEnum.TOKEN_EMPTY);
        }
        try {
            Object o = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey(tokenSignKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get(CommonConstants.USER);
            String jsonString = gson.toJson(o);
            return gson.fromJson(jsonString, JwtUser.class);
        } catch (SecurityException se) {
            msg = "密钥错误";
            log.error(msg, se);
            throw new BaseException(CommonResponseEnum.TOKEN_INVALID);

        }catch (MalformedJwtException me) {
            msg = "密钥算法或者密钥转换错误";
            log.error(msg, me);
            throw new BaseException(CommonResponseEnum.TOKEN_INVALID);

        }catch (MissingClaimException mce) {
            msg = "密钥缺乏校验数据";
            log.error(msg, mce);
            throw new BaseException(CommonResponseEnum.TOKEN_INVALID);

        }catch (ExpiredJwtException mce) {
            msg = "密钥已过时";
            log.error(msg, mce);
            throw new BaseException(CommonResponseEnum.TOKEN_KEY_EXPIRED);

        }catch (JwtException jwte) {
            msg = "密钥解析错误";
            log.error(msg, jwte);
            throw new BaseException(CommonResponseEnum.TOKEN_INVALID);
        }
        catch (Exception e) {
            msg = "系统错误";
            log.error(msg, e);
            throw new BaseException(CommonResponseEnum.SYS_ERROR);
        }
    }
}

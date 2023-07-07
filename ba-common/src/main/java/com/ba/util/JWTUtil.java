package com.ba.util;

import com.ba.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author zouzujiang
 */
@Slf4j
public class JWTUtil {

    /**
     * token 过期时间，正常是7天，方便测试我们改为70
     */
    private static final long EXPIRE = 1000L * 60 * 60 * 24 * 7 * 10;

    /**
     * 加密的秘钥
     */
    private static final String SECRET = "smart.catering.2022";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "smartcatering2022";

    /**
     * subject
     */
    private static final String SUBJECT = "smart";


    /**
     * 根据用户信息，生成令牌
     *
     * @param loginUser
     * @return
     */
    public static String geneJsonWebToken(UserInfo loginUser) {

        if (loginUser == null) {
            throw new NullPointerException("loginUser对象为空");
        }

        String token = Jwts.builder().setSubject(SUBJECT)
                //payload
                .claim("userId", loginUser.getUserId())
                .claim("userCode", loginUser.getUserCode())
                .claim("userName", loginUser.getUserName())
                .claim("realName", loginUser.getRealName())
                .claim("email", loginUser.getEmail())
                .claim("telephone", loginUser.getTelephone())
                .claim("roleId", loginUser.getRoleId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
        token = TOKEN_PREFIX + token;
        return token;
    }


    /**
     * 校验token的方法
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
            return claims;
        } catch (Exception e) {
            log.info("jwt token解密失败");
            return null;
        }
    }
}

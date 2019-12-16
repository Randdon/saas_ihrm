package com.zhouyuan.saas.ihrm.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

/**
 * @description: jwt工具类
 * @author: yuand
 * @create: 2019-12-16 16:42
 **/
//@ConfigurationProperties(value = "jwt.config")
public class JwtUtils {

    //签名私钥
    private String key;

    //过期时间
    private long ttl;

    /**
     * 生成jwt
     * @param userId
     * @param userName
     * @param map 自定义token内容
     * @return token
     */
    public String createJwt(String userId, String userName, Map<String,Object> map){

        //构造过期时间
        long exp = System.currentTimeMillis();
        exp += ttl;

        JwtBuilder jwtBuilder = Jwts.builder().setId(userId)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key)
                .addClaims(map);

        jwtBuilder.setExpiration(new Date(exp));

        return jwtBuilder.compact();
    }

    /**
     * 解析jwt
     * @param token
     * @return
     */
    public Claims parseJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }

}

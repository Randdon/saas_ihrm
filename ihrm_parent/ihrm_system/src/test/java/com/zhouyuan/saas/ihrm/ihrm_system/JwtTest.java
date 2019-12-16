package com.zhouyuan.saas.ihrm.ihrm_system;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

/**
 * @description: Json Web Token测试
 * @author: yuand
 * @create: 2019-12-16 15:15
 **/
public class JwtTest extends IhrmSystemApplicationTests {

    /**
     * 生成token
     */
    @Test
    public void createJwt(){
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("1893")//设置token里的一些内容
                .setSubject("ChairmanMao")//设置token里的一些内容
                .setIssuedAt(new Date())//设置token的签发时间
                .signWith(SignatureAlgorithm.HS256, "ihrm");//加密算法和私钥
        String token = jwtBuilder.compact();
        System.out.println(token);
    }

    /**
     * 解析token
     */
    @Test
    public void parseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODkzIiwic3ViIjoiQ2hhaXJtYW5NYW8iLCJpYXQiOjE1NzY0ODEwMDd9.gAMxX0qDegM-Y162fDKEJzZCSetN7PxcBZchE1qH2A0";
        Claims claims = Jwts.parser()
                .setSigningKey("ihrm")//私钥
                //TODO 此处解析时所用的密钥，如果为原密钥后面+3位的都可以解析成功，比如ihrm123，ihrmab，ihrmc都可以解析出数据
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
    }
}

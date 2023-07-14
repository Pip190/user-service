package com.chongdong.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static long time = 1000*60*60*1;
    private static String sign = "admin";
    public static String createToken(){
        // 创建一个JwtBuilder对象
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                // header：头部
                .setHeaderParam("type","JWT")
                .setHeaderParam("alg","HS256")
                // Payload：载荷
                .claim("username","tom")
                .claim("role","admin")
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis()+ time))//Token的过期时间
                .setId(UUID.randomUUID().toString())//id字段
                //sign:签名
                .signWith(SignatureAlgorithm.HS256,sign)//设置加密算法和签名
                //使用"."连接成一个完整的字符串
                .compact();
        return jwtToken;
    }

    public static boolean checkToken(String token){
        if(token == null || token == ""){
            return false;   // false表示过期
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(sign).parseClaimsJws(token);
        }catch (Exception e){
            e.printStackTrace();
            return false;   // false表示过期
        }
        return true;
    }
}

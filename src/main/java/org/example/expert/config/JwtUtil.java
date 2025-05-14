package org.example.expert.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000L * 60 * 60; //60분
    @Value("${jwt.secret.key}")
    private String secretKey;


    //토큰 발급 (userNickName 추가 )
    public String createToken(String email,Long userId,String userNickName, UserRole userRole) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("userNickName", userNickName)
                .claim("userRole", userRole)
                .setIssuedAt(date) // 발급일
                .setExpiration(new Date(date.getTime() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    //토큰 검증
    public Claims validateToken(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch(JwtException e){
            return null;
        }

    }

}

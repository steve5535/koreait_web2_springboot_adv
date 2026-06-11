package com.study.websocket.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long expireMillis;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expire-millis}") long expireMillis
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expireMillis = expireMillis;
    }

    // 발급
    public String createToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expireMillis))
                .signWith(key)
                .compact();
    }

    // 검증
    public String validateTokenAndGetUsername(String token) {
        
        Claims claims = Jwts.parser()
                .verifyWith(key) // 위조검증
                .build()
                .parseSignedClaims(token) // 페이로드 꺼내기
                .getPayload();
        return claims.getSubject();
    }
    
    // 헤더에서 토큰 추출 
    public String resolveToken(String authHeader) {
        if (authHeader == null) {
            throw new IllegalArgumentException("Authorization 헤더 없음");
        }
        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Bearer 토큰 형식이 아닙니다");
        }

        return authHeader.substring("Bearer ".length());
    }

    

}

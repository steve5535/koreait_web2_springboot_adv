package com.study.websocket.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private final SecretKey key;
    private final long expireMillis;

    public JwtUtil(SecretKey key,
                   @Value("${jwt.secret}") String secret,
                   @Value("${jwt.expire-millis") long expireMillis
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

}

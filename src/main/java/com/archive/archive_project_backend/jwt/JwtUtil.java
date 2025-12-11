package com.archive.archive_project_backend.jwt;

import com.archive.archive_project_backend.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Map;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long accessExpireMillis;
    private final long refreshExpireMillis;

    public JwtUtil(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.access-expire-millis}") long accessExpireMillis,
        @Value("${jwt.refresh-expire-millis}") long refreshExpireMillis
    ){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessExpireMillis = accessExpireMillis;
        this.refreshExpireMillis = refreshExpireMillis;
    }

    //토큰 생성기
    private String buildToken(
            String subject,
            long expireMillis,
            Map<String, Object> extraClaims,
            String type
    ){
        //시간 계산
        long now = System.currentTimeMillis();
        long exp = now + expireMillis;

        //빌드
        JwtBuilder builder = Jwts.builder()
                .claim("sub", subject)
                .claim("iat", now / 1000) //발급시간
                .claim("exp", exp / 1000)
                .claim("type", type);

        //payload에 데이터 저장
        if(extraClaims != null){
            extraClaims.forEach(builder::claim);
        }

        return builder.signWith(key).compact();
    }

    //각각의 토큰 생성
    public String generateAccessToken(String subject, Map<String, Object> extraClaims){
        return buildToken(subject, accessExpireMillis, extraClaims, JwtConstants.ACCESS);
    }
    public String generateRefreshToken(String subject, Map<String, Object> extraClaims){
        return buildToken(subject, refreshExpireMillis, extraClaims, JwtConstants.REFRESH);
    }

    //토큰 검증 및 claim 추출
    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Bearer 검사
    public boolean isBearer(String header){
        return header.startsWith(JwtConstants.BEARER);
    }

    //Bearer 제거
    public String removeBearer(String header){
        return header.substring(JwtConstants.BEARER.length());
    }


}

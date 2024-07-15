package com.study.event.api.auth;

import com.study.event.api.event.entity.EventUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
// 토큰을 생성하여 발급하고, 서명 위조를 검사하는 객체
public class TokenProvider {
    // 서명에 사용할 512비트의 랜덤 문자열 비밀키
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    /**
     * JWT를 생성하는 메서드
     * @param eventUser - 토큰에 포함될 로그인한 유저의 정보
     * @return - 생성된 JWT의 암호화된 문자열
     */
    public String createToken(EventUser eventUser){
        /*
            토큰의 형태
            {
                "iss": "뽀로로로월드"
                "exp": "2024-07-18",
                "iat": "2024-07-15",
                ...
                "email": "로그인한 사람 이메일",
                "role": "ADMIN"
                ...

                ===

                서명
            }
         */

        // 토큰에 들어갈 커스텀 데이터 (추가 클레임)
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", eventUser.getEmail());
        claims.put("role", eventUser.getRole().toString());

        return Jwts.builder()
                // token에 들어갈 서명
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                        , SignatureAlgorithm.HS512
                )
                .setClaims(claims) // 추가 클레임은 항상 가장 먼저 설정
                .setIssuer("hihihihi")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .setSubject(eventUser.getId()) // 토큰을 식별할 수 있는 유일한 값
                .compact();
    }

    /**
     * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 서명 위조 여부를 확인
     * 그리고 토큰을 JSON으로 파싱하여 안에 들어있는 클레임(토큰 정보)을 리턴
     * @param token
     */
    public void validateAndGetTokenInfo(String token) {
        Claims claims = Jwts.parserBuilder()
                // 토큰 발급자의 발급 당시 서명을 넣음
                .setSigningKey(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                )
                // 서명 위조 검사 진행 : 위조된 경우 Exception이 발생
                // 위조 되지 않은 경우 클레임을 리턴
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("claim: {}", claims);
    }
}
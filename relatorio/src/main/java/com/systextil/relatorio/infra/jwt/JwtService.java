package com.systextil.relatorio.infra.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtService {
	
	@Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(dataExpiracao())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getUsernameToken(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        
        return jwt.getSubject();
    }
    
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
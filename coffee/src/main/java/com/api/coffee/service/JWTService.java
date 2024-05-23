package com.api.coffee.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JWTService {
    @Value("${application.security.jwt.secret_key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(Authentication user){
        Date tokenCreateDate = new Date();
        Date tokenExpirationDate = new Date(tokenCreateDate.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .claims()
                .subject(user.getName())
                .expiration(tokenExpirationDate)
                .and()
                .signWith(getSignInKey())
                .compact();
        return token;
    }

    public String extractEmail(String token){
        try{
            Claims claims = decodeToken(token);
            return claims.getSubject();
        }catch(Exception e){
            return null;
        }
    }

    private Claims decodeToken(String token) throws Exception {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
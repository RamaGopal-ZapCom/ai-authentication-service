package com.zapcom.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;





@Component
@Slf4j
public class JwtUtilService {

    @Value("${jwt.EXPIRATION_TIME}")
    private long EXPIRATION_TIME;
    private Key secretKey;
    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;


    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256);
        SecretKey generatedKey = keyGenerator.generateKey();
        this.secretKey = Keys.hmacShaKeyFor(generatedKey.getEncoded());
    }

    public String generateToken(Authentication authentication) {
        log.info("generate token " + EXPIRATION_TIME);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}

package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;

@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private final String s_key;
    @Value("${application.security.jwt.expiration}")
    private final long jwt_expiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private final long jwt_refresh_expiration;

    public String generateAccessToken(UserEntity user){
        return buildToken(user, jwt_expiration);
    }

    public String generateRefreshToken(UserEntity user){
        return buildToken(user, jwt_refresh_expiration);
    }

    private String buildToken(UserEntity user, long expiration){
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(Map.ofEntries(entry("name", user.getUsername()), entry("role", user.getUserRole())))
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(s_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

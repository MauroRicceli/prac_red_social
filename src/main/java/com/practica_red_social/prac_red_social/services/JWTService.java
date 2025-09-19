package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import io.jsonwebtoken.Claims;
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
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private String s_key;
    @Value("${application.security.jwt.expiration}")
    private long jwt_expiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwt_refresh_expiration;

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

    public String extractTokenUsername(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

        return jwtToken.getSubject();
        //PARA OBTENER EL CLAIM OPCIONAL QUE YO GUARDE EN EL TOKEN, COMO EL USERNAME O EL ROL COMO HASHMAP.
        //return jwtToken.get("role", String.class);
    }

    //verificar que devuelve el subject, por que tambien hay roles en el token.
    public String isValidToken(String token){
        String username = extractTokenUsername(token);
        return "aus";
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(s_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

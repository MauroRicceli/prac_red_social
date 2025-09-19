package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.exceptions.InvalidTokenExpirated;
import com.practica_red_social.prac_red_social.exceptions.InvalidTokenType;
import com.practica_red_social.prac_red_social.exceptions.InvalidTokenUserDontExists;
import com.practica_red_social.prac_red_social.exceptions.InvalidTokenUserRoleDoesntMatch;
import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import com.practica_red_social.prac_red_social.repositories.TokenRepository;
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
import java.util.Optional;
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

    private TokenRepository tokenRepository;

    public String generateAccessToken(UserEntity user){
        return buildToken(user, jwt_expiration, "access");
    }

    public String generateRefreshToken(UserEntity user){

        return buildToken(user, jwt_refresh_expiration, "refresh");
    }

    private String buildToken(UserEntity user, long expiration, String type){
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(Map.ofEntries(entry("name", user.getUsername()), entry("role", user.getUserRole()), entry("type", type)))
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

    private String extractTokenRole(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        return jwtToken.get("role", String.class);
    }

    private String extractTokenType(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        return jwtToken.get("type", String.class);
    }

    private Date extractTokenExpiration(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        return jwtToken.getExpiration();
    }

    //verificar que devuelve el subject, por que tambien hay roles en el token.
    public boolean isValidRefreshToken(String token){
        String username = extractTokenUsername(token);

        if(!extractTokenType(token).equals("refresh")){
            throw new InvalidTokenType("El token no es de tipo refresh");
        }

        final Optional<TokenEntity> tkn = tokenRepository.findByUserEmail(username);

        if(tkn.isEmpty()){
            throw new InvalidTokenUserDontExists("El usuario que envia ese token no existe");
        }

        final UserEntity userTkn = tkn.get().getUsuarioPertenece();

        if(!userTkn.getUserRole().toString().equals(extractTokenRole(token))){
            throw new InvalidTokenUserRoleDoesntMatch("El rol enviado en el token y el del usuario son distintos");
        }

        if(isTokenExpired(token)){
           throw new InvalidTokenExpirated("El token está expirado");
        }

        return true;

    }

    //no tiene que consultar a la DB si es access
    public boolean isValidAccessToken(String token){
        if(!extractTokenType(token).equals("access")){
            throw new InvalidTokenType("El token no es de tipo access");
        }
        if(isTokenExpired(token)){
            throw new InvalidTokenExpirated("El token está expirado");
        }
        return true;
    }

    private boolean isTokenExpired(String token){
        return extractTokenExpiration(token).before(new Date());
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(s_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

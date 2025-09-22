package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.exceptions.*;
import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import com.practica_red_social.prac_red_social.repositories.TokenRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    public String generateAccessToken(UserEntity user){
        return buildToken(user, jwt_expiration, "access");
    }

    public String generateRefreshToken(UserEntity user){

        return buildToken(user, jwt_refresh_expiration, "refresh");
    }


    /**
     * Genera un token válido para el usuario
     *
     * @param user Usuario enviado
     * @param expiration Expiracion pretendida para el token
     * @param type Tipo de token, acceso/refresh
     * @return String token construido
     */
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

    /**
        Obtiene el email del usuario dueño del token

        @param token Token enviado
        @return String con el email del usuario
    **/
    public String extractTokenUsername(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

        return jwtToken.getSubject();
        //PARA OBTENER EL CLAIM OPCIONAL QUE YO GUARDE EN EL TOKEN, COMO EL USERNAME O EL ROL COMO HASHMAP.
        //return jwtToken.get("role", String.class);
    }



    /**
     * Obtiene el rol del usuario dueño del token
     *
     * @param token Token enviado
     * @return String con el rol del usuario
     */

    public String extractTokenRole(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        return jwtToken.get("role", String.class);
    }

    /**
     * Obtiene el tipo de token enviado (refresh o access)
     *
     * @param token Token enviado
     * @return String tipo de token
     */

    private String extractTokenType(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        return jwtToken.get("type", String.class);
    }

    /**
     * Obtiene la expiracion del token
     *
     * @param token
     * @return Date fecha de expiracion
     */
    private Date extractTokenExpiration(String token){
        Claims jwtToken = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        return jwtToken.getExpiration();
    }

    /**
     * Verifica que el token de refresco sea completamente valido
     *
     * @param token Token de REFRESCO enviado
     * @return boolean True/excepcion si es valido o no.
     */
    private boolean isValidRefreshToken(String token){
        String username = extractTokenUsername(token);

        final Optional<TokenEntity> tkn = tokenRepository.findByToken(token);

        if(tkn.isEmpty()){
            throw new InvalidTokenDontExists("El token no existe en el sistema");
        }

        final UserDetails usrDetails = userDetailsService.loadUserByUsername(username);
        final UserEntity user = userRepository.getUserByEmail(usrDetails.getUsername())
                .orElseThrow(() -> new InvalidTokenUserDontExists("El usuario enviado en el token no coincide con ninguno en el sistema"));


        if(!user.getUserRole().toString().equals(extractTokenRole(token))){
            throw new InvalidTokenUserRoleDoesntMatch("El rol enviado en el token y el del usuario son distintos");
        }

        //verifico que el token enviado y el de la base de datos no estén expirados.
        if(isTokenExpired(token) || tkn.get().isExpired()){
           throw new InvalidTokenExpirated("El token está expirado");
        }

        if(tkn.get().isRevoked()){
            throw new InvalidTokenRevoked("El token ha sido revocado");
        }

        return true;

    }

    /**Verifica que el token enviado sea completamente valido
     *
     *
     * @param header Header enviado
     * @return true/false si es válido o no.
     */

    public boolean isValidHeader(String header){

        if(header == null || !header.contains("Bearer ")){
            throw new InvalidTokenType("El header es nulo o no es de tipo Bearer");
        }

        String token = header.substring(7);

        if(extractTokenType(token).toLowerCase().equals("refresh")){
            return isValidRefreshToken(token);
        } else if(extractTokenType(token).toLowerCase().equals("access")) {
            return isValidAccessToken(token);
        } else {
            throw new InvalidTokenType("El tipo de token es inválido");
        }
    }

    /**
     * Verifica que el token de acceso sea valido o no
     *
     * @param token Token de ACCESO enviado
     * @return true/excepcion depende si es valido o no.
     */
    private boolean isValidAccessToken(String token){

        if(isTokenExpired(token)){
            throw new InvalidTokenExpirated("El token está expirado");
        }
        if(!extractTokenUsername(token).contains("@")){
            throw new InvalidTokenUserDontExists("El usuario enviado no es válido");
        }
        if(!extractTokenRole(token).equals("ADMIN") && !extractTokenRole(token).equals("STANDARD")){
            throw new InvalidTokenUserRoleDoesntMatch("El usuario no tiene un rol válido");
        }

        return true;
    }

    /**
     * Verifica si el token esta expirado
     *
     * @param token Token enviado
     * @return True/False dependiendo si es valido o no.
     */
    private boolean isTokenExpired(String token){
        return extractTokenExpiration(token).before(new Date());
    }


    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(s_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

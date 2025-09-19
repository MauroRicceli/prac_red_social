package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.configs.EncryptConfig;
import com.practica_red_social.prac_red_social.exceptions.IllegalLoginEmailDoesntExists;
import com.practica_red_social.prac_red_social.exceptions.IllegalLoginPasswordDoesntMatches;
import com.practica_red_social.prac_red_social.models.dtos.LoginRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.RegisterRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.ResponseTokenDTO;
import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;

import com.practica_red_social.prac_red_social.repositories.TokenRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final EncryptConfig encryptConfig;
    private final JWTService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public ResponseTokenDTO register(RegisterRequestDTO registerRequest){
       final String hashpw = encryptConfig.obtenerEncriptador().encode(registerRequest.getPassword());
       final UserEntity user = UserEntity
               .builder()
               .username(registerRequest.getUsername())
               .userRole(UserEntity.UserRole.valueOf(registerRequest.getRole()))
               .email(registerRequest.getEmail())
               .password(hashpw)
               .build();

       final String accessToken = jwtService.generateAccessToken(user);
       final String refreshToken = jwtService.generateRefreshToken(user);

       final TokenEntity token = TokenEntity
               .builder()
               .tokenType(TokenEntity.TokenType.BEARER)
               .expired(false)
               .revoked(false)
               .token(refreshToken)
               .usuarioPertenece(user)
               .build();

       userRepository.save(user);
       tokenRepository.save(token);

       return new ResponseTokenDTO(accessToken, refreshToken);

    }

    public ResponseTokenDTO login(LoginRequestDTO loginRequest){
        UserEntity user = userRepository.getUserByEmail(loginRequest.getEmail()).orElseThrow();

        /*  NO HACE FALTA VERIFICAR SI EXISTE EL MAIL, O LA CONTRASEÑA COINCIDE. EL AUTH. MANAGER LO HACE SOLO APOYANDOSE EN USERDETAILSSERVICE Y PASSWORD ENCODER.
        if(!encryptConfig.obtenerEncriptador().matches(loginRequest.getPassword(), user.getPassword())){
            throw new IllegalLoginPasswordDoesntMatches("Las contraseñas no coinciden");
        }
         */

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        tokenRepository.revokeOrExpireTokenByUser(user);

        final String accessToken = jwtService.generateAccessToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);

        TokenEntity tkn = TokenEntity.builder()
                .tokenType(TokenEntity.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .usuarioPertenece(user)
                .token(refreshToken)
                .build();

        tokenRepository.save(tkn);

        return new ResponseTokenDTO(accessToken, refreshToken);
    }

}

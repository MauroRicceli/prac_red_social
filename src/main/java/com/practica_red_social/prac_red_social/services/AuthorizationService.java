package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.configs.EncryptConfig;
import com.practica_red_social.prac_red_social.controllers.AuthController;
import com.practica_red_social.prac_red_social.models.dtos.RegisterRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.ResponseTokenDTO;
import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;

import com.practica_red_social.prac_red_social.repositories.TokenRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final EncryptConfig encryptConfig;
    private final JWTService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public ResponseTokenDTO register(RegisterRequestDTO registerRequest){
       final String hashpw = encryptConfig.codificarPassword().encode(registerRequest.getPassword());
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

    //Testear como me devuelve el username del token al ponerle roles en el Claims.
    public String test(){
        List<UserEntity> usr = userRepository.findAll();

        final Optional<TokenEntity> token = tokenRepository.findByUser(usr.get(0));

        if(token.isEmpty()){
            throw new IllegalArgumentException("No existe el token");
        }

        return jwtService.extractTokenUsername(token.get().getToken());
    }
}

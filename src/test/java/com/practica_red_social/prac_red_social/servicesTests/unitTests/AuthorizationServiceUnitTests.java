package com.practica_red_social.prac_red_social.servicesTests.unitTests;


import com.practica_red_social.prac_red_social.configs.EncryptConfig;
import com.practica_red_social.prac_red_social.models.dtos.LoginRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.RegisterRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.ResponseTokenDTO;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import com.practica_red_social.prac_red_social.repositories.TokenRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import com.practica_red_social.prac_red_social.services.AuthorizationService;
import com.practica_red_social.prac_red_social.services.JWTService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

//(@SpringBootTest SOLO SI QUIERO CARGARTODO EL CONTEXTO DE SPRING BOOT.
@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceUnitTests {

    @Mock
    private EncryptConfig encryptConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthorizationService authorizationService;

    @BeforeEach
    public void setup(){
        Mockito.when(jwtService.generateAccessToken(any(UserEntity.class))).thenReturn("tokenaccess");
        Mockito.when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn("tokenrefresh");
    }

    @Test
    @DisplayName("Mocked verify register")
    public void verificarRegisterMockeado(){

        RegisterRequestDTO registerRequest = new RegisterRequestDTO("testuser@gmail.com", "testing123", "testingpassword", "ADMIN");

        Mockito.when(encryptConfig.obtenerEncriptador()).thenReturn(passwordEncoder);
        Mockito.when(encryptConfig.obtenerEncriptador().encode(any(CharSequence.class))).thenReturn("password");

        ResponseTokenDTO retTest = authorizationService.register(registerRequest);



        Assertions.assertEquals(new ResponseTokenDTO("tokenaccess","tokenrefresh"), retTest);

    }

    @Test
    @DisplayName("Mocked verify login")
    public void verificarLoginMockeado(){
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser@gmail.com", "testing123");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        Mockito.when(userRepository.getUserByEmail(any(String.class))).thenReturn(Optional.of(new UserEntity()));

        ResponseTokenDTO retTest = authorizationService.login(loginRequest);

        Assertions.assertEquals(new ResponseTokenDTO("tokenaccess","tokenrefresh"), retTest);

    }
    @Test
    @DisplayName("Mocked verify refresh token")
    public void verificarRefreshMockeado(){
        String header = new String("Bearer tokendetest");

        Mockito.when(jwtService.extractTokenUsername(any(String.class))).thenReturn("testing");
        Mockito.when(userRepository.getUserByEmail(any(String.class))).thenReturn(Optional.of(new UserEntity()));

        ResponseTokenDTO retTest = authorizationService.renovateTokens(header);

        Assertions.assertEquals(new ResponseTokenDTO("tokenaccess", "tokenrefresh"), retTest);
    }




}

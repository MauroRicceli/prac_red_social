package com.practica_red_social.prac_red_social.servicesTests.integrationTests;

import com.practica_red_social.prac_red_social.exceptions.*;
import com.practica_red_social.prac_red_social.models.dtos.RegisterRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.ResponseTokenDTO;
import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import com.practica_red_social.prac_red_social.repositories.testsrepos.TokenRepositoryTests;
import com.practica_red_social.prac_red_social.repositories.testsrepos.UserRepositoryTests;
import com.practica_red_social.prac_red_social.services.AuthorizationService;
import com.practica_red_social.prac_red_social.services.JWTService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SpringBootTest
public class JWTServiceIntegrationTest {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthorizationService authorizationService;

     /*  {
            "email": "johndoe@gmail.com",
            "username": "John Doe",
            "password":"password",
            "role":"ADMIN"
            }   */


    @Autowired
    private TokenRepositoryTests tokenRepository;

    @Autowired
    private UserRepositoryTests userRepository;

    private final String tokenDeAccesoTest = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ZjhkNzMwZS05ZTJjLTQxMDMtYTI1MC1mMjFlODY0ZTFlYWUiLCJuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiQURNSU4iLCJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiam9obmRvZUBnbWFpbC5jb20iLCJpYXQiOjE3NTg0OTQ5MzksImV4cCI6MjQ3OTM5NTE2OX0.zIlHc_fozxVLvvy-TBcB6cVNcSGdL4Jn3pPf1ZePXIc";
    private final String tokenDeRefrescoTest = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyYjAyZmE3Mi01NTAzLTQzZTMtOGMyOC0yMmE5ZDFlZGI5ZWEiLCJuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiQURNSU4iLCJ0eXBlIjoicmVmcmVzaCIsInN1YiI6ImpvaG5kb2VAZ21haWwuY29tIiwiaWF0IjoxNzU4NDk0OTM5LCJleHAiOjI0Mzk4MDg5NTE2OX0.YN2bELt4vszsFbMmcyaXyxthJiBLNYkDFFdSRNkAZ-8";


    @BeforeAll
    public static void firstSetup(){
        //CARGO MIS VARIABLES DE ENTORNO.
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

    }

    @BeforeEach
    public void eachSetup(){
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Extract token username valid")
    public void extractTokenUsernameTest(){
        String usernameTknAccess = jwtService.extractTokenUsername(tokenDeAccesoTest);
        String usernameTknRefresh = jwtService.extractTokenUsername(tokenDeRefrescoTest);

        Assertions.assertEquals("johndoe@gmail.com", usernameTknAccess);
        Assertions.assertEquals("johndoe@gmail.com", usernameTknRefresh);
    }

    @Test
    @DisplayName("Extract token username invalid")
    public void extractTokenUsernameTestInvalid(){
        String usernameTknAccess = jwtService.extractTokenUsername(tokenDeAccesoTest);
        String usernameTknRefresh = jwtService.extractTokenUsername(tokenDeRefrescoTest);

        Assertions.assertNotEquals("johndoegmail.com", usernameTknAccess);
        Assertions.assertNotEquals("johndoegmail.com", usernameTknRefresh);
    }

    @Test
    @DisplayName("Extract token role valid")
    public void extractTokenRole(){
        String roleTknAccess = jwtService.extractTokenRole(tokenDeAccesoTest);
        String roleTknRefresh = jwtService.extractTokenRole(tokenDeRefrescoTest);

        Assertions.assertEquals("ADMIN", roleTknAccess);
        Assertions.assertEquals("ADMIN", roleTknRefresh);
    }

    @Test
    @DisplayName("Extract token role invalid")
    public void extractTokenRoleInvalid(){
        String roleTknAccess = jwtService.extractTokenRole(tokenDeAccesoTest);
        String roleTknRefresh = jwtService.extractTokenRole(tokenDeRefrescoTest);

        Assertions.assertNotEquals("STANDARD", roleTknAccess);
        Assertions.assertNotEquals("STANDARD", roleTknRefresh);
    }

    @Test
    @DisplayName("Generate access token valid")
    public void generateAccessToken(){
        UserEntity usr = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();
        String tknAccess = jwtService.generateAccessToken(usr);

        Assertions.assertEquals("johndoe@gmail.com", jwtService.extractTokenUsername(tknAccess));
        Assertions.assertEquals("ADMIN", jwtService.extractTokenRole(tknAccess));
    }

    @Test
    @DisplayName("Generate access token Invalid")
    public void generateAccessTokenInvalid(){
        UserEntity usr = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();
        String tknAccess = jwtService.generateAccessToken(usr);

        Assertions.assertNotEquals("johndoegmail.com", jwtService.extractTokenUsername(tknAccess));
        Assertions.assertNotEquals("STANDARD", jwtService.extractTokenRole(tknAccess));
    }

    @Test
    @DisplayName("Generate refresh token valid")
    public void generateRefreshToken(){
        UserEntity usr = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();
        String tknRefresh = jwtService.generateRefreshToken(usr);
        Assertions.assertEquals("johndoe@gmail.com", jwtService.extractTokenUsername(tknRefresh));
        Assertions.assertEquals("ADMIN", jwtService.extractTokenRole(tknRefresh));
    }

    @Test
    @DisplayName("Generate refresh token invalid")
    public void generateRefreshTokenInvalid(){
        UserEntity usr = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();
        String tknRefresh = jwtService.generateRefreshToken(usr);
        Assertions.assertNotEquals("johndoegmail.com", jwtService.extractTokenUsername(tknRefresh));
        Assertions.assertNotEquals("STANDARD", jwtService.extractTokenRole(tknRefresh));
    }

    @Test
    @DisplayName("Valid auth header with access token")
    public void validHeaderAccessTokenType(){
        String header = "Bearer ".concat(tokenDeAccesoTest);
        Assertions.assertTrue(jwtService.isValidHeader(header));
    }

    @Test
    @DisplayName("Invalid auth header with token (No bearer)")
    public void validHeaderTokenTypeInvalidType(){
        String header = "BEARER ".concat(tokenDeAccesoTest);
        Assertions.assertThrowsExactly(InvalidTokenType.class, () -> { throw new InvalidTokenType(""); });
    }

    @Test
    @DisplayName("Valid auth header with refresh token")
    public void validHeaderRefreshTokenType(){
        ResponseTokenDTO tkn = authorizationService.register(new RegisterRequestDTO("johndoe@gmail.com", "John Doe", "password", "ADMIN"));

        String header = "Bearer ".concat(tkn.getRefreshToken());

        Assertions.assertTrue(jwtService.isValidHeader(header));
    }

    @Test
    @DisplayName("Invalid auth header with expirated access token")
    public void invalidHeaderExpiratedAccessToken(){
        String header = "Bearer ".concat("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1NTI3MjZkZS1jMzExLTQyNDMtYjhkYy03MTE4ZmUxMGRmMjYiLCJ0eXBlIjoiYWNjZXNzIiwicm9sZSI6IkFETUlOIiwibmFtZSI6IkpvaG4gRG9lIiwic3ViIjoiam9obmRvZUBnbWFpbC5jb20iLCJpYXQiOjE3NTg0OTg0MjksImV4cCI6MTc1ODQ5ODQyOX0.FSvjYRBgLakyBEsrpz3w8e1gMv6Sg8c7XM2TMPCDIBs");

        Assertions.assertThrowsExactly(ExpiredJwtException.class, () -> { jwtService.isValidHeader(header); });
    }

    @Test
    @DisplayName("Not trusted token in header")
    public void notTrustedTokenInHeader(){
        String header = "Bearer ".concat("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJURVNUIiwiaWF0IjoxNzU4NDk2OTk3LCJleHAiOjE3NTg0OTY5OTcsImF1ZCI6InRlc3RpbmciLCJzdWIiOiJ0ZXN0aW5nIiwidHlwZSI6ImFjY2VzcyJ9.OhQBOrDgyqBVD3w9_ccMCgao-2DBcn-CL6ShKjO8YG8");

        Assertions.assertThrowsExactly(SignatureException.class, () -> { jwtService.isValidHeader(header); });
    }

    @Test
    @DisplayName("Invalid auth header with wrong type of token")
    public void invalidHeaderWrongTypeToken(){
        String header = "Bearer ".concat("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4OTZlM2MzMC02NDg4LTRmNDgtOTAwNC1jMDdiMTRhMWU1MWUiLCJ0eXBlIjoic210aCIsInJvbGUiOiJBRE1JTiIsIm5hbWUiOiJKb2huIERvZSIsInN1YiI6ImpvaG5kb2VAZ21haWwuY29tIiwiaWF0IjoxNzU4NTIyODkxLCJleHAiOjQ3ODI2MjE5MTV9.s-F8Tl2H4u7U_FTELxiuvAPBbQqVm629W8d6K7zRaic");

        Assertions.assertThrowsExactly(InvalidTokenType.class, () -> { jwtService.isValidHeader(header); });
    }

    @Test
    @DisplayName("Invalid auth header with unknown token")
    public void invalidHeaderUnknownToken(){
        String header = "Bearer ".concat(tokenDeRefrescoTest);

        Assertions.assertThrowsExactly(InvalidTokenDontExists.class, () -> { jwtService.isValidHeader(header); });
    }

    @Test
    @DisplayName("Invalid auth header with unknown user")
    public void invalidHeaderWithUnknownUser(){
        UserEntity usr = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();
        String header = "Bearer ".concat(tokenDeRefrescoTest);

        //si no esta en la base, no apunta a nada
        Assertions.assertThrowsExactly(InvalidDataAccessApiUsageException.class, () -> { tokenRepository.save(TokenEntity.builder().token(tokenDeRefrescoTest).tokenType(TokenEntity.TokenType.BEARER).expired(false).revoked(false).usuarioPertenece(usr).build()); });

    }

    @Test
    @DisplayName("Invalid auth header with different roles than user")
    public void invalidHeaderDifferentRolesWithUser(){
        UserEntity usr = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();

        String header = "Bearer ".concat(tokenDeRefrescoTest);
        userRepository.save(usr);
        userRepository.updateRole(usr.getEmail(), UserEntity.UserRole.STANDARD);
        tokenRepository.save(TokenEntity.builder().token(tokenDeRefrescoTest).tokenType(TokenEntity.TokenType.BEARER).expired(false).revoked(false).usuarioPertenece(usr).build());

        //Como no está en la base el usuario con el nuevo rol, no deja.
        Assertions.assertThrowsExactly(InvalidTokenUserRoleDoesntMatch.class, () -> {jwtService.isValidHeader(header);});
    }

    @Test
    @DisplayName("Invalid auth header refresh token is revoked")
    public void invalidHeaderRefreshTokenRevoked(){
        UserEntity usr = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();

        String header = "Bearer ".concat(tokenDeRefrescoTest);

        userRepository.save(usr);

        tokenRepository.save(TokenEntity.builder().token(tokenDeRefrescoTest).tokenType(TokenEntity.TokenType.BEARER).expired(false).revoked(true).usuarioPertenece(usr).build());

        Assertions.assertThrowsExactly(InvalidTokenRevoked.class, () -> {jwtService.isValidHeader(header);});
    }



}

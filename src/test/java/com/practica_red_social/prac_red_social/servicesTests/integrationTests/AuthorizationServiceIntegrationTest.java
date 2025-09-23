package com.practica_red_social.prac_red_social.servicesTests.integrationTests;

import com.practica_red_social.prac_red_social.models.dtos.LoginRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.RegisterRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.ResponseTokenDTO;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import com.practica_red_social.prac_red_social.repositories.testsrepos.TokenRepositoryTests;
import com.practica_red_social.prac_red_social.repositories.testsrepos.UserRepositoryTests;
import com.practica_red_social.prac_red_social.services.AuthorizationService;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorizationServiceIntegrationTest {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private TokenRepositoryTests tokenRepository;

    @Autowired
    private UserRepositoryTests userRepository;

    @BeforeAll
    public static void firstSetup(){
        //CARGO MIS VARIABLES DE ENTORNO.
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    @BeforeEach
    public void setupEach(){
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Valid register test")
    public void validRegister(){
        RegisterRequestDTO request = RegisterRequestDTO.builder().email("johndoe@gmail.com").role("ADMIN").password("password").username("John Doe").build();

        Assertions.assertInstanceOf(ResponseTokenDTO.class,authorizationService.register(request));
        Assertions.assertInstanceOf(UserEntity.class,userRepository.getUserByEmail("johndoe@gmail.com").get());
    }

    @Test
    @DisplayName("Invalid register repeated email")
    public void invalidRegisterRepeatedEmail(){
        RegisterRequestDTO request = RegisterRequestDTO.builder().email("johndoe@gmail.com").role("ADMIN").password("password").username("John Doe").build();
        RegisterRequestDTO request2 = RegisterRequestDTO.builder().email("johndoe@gmail.com").role("STANDARD").password("password").username("Mary Poppins").build();

        authorizationService.register(request);
        Assertions.assertThrows(RuntimeException.class, () -> {authorizationService.register(request2);});
    }

    @Test
    @DisplayName("Valid login test")
    public void validLogin(){
        RegisterRequestDTO request = RegisterRequestDTO.builder().email("johndoe@gmail.com").role("ADMIN").password("password").username("John Doe").build();

        authorizationService.register(request);

        Assertions.assertInstanceOf(ResponseTokenDTO.class, authorizationService.login(new LoginRequestDTO("johndoe@gmail.com","password")));
    }

    @Test
    @DisplayName("Invalid login user doesnt exists ")
    public void InvalidLoginUserNotExists(){
        LoginRequestDTO dto = new LoginRequestDTO("johndoe@gmail.com","password");

        Assertions.assertThrows(RuntimeException.class, () -> {authorizationService.login(dto);});
    }

    @Test
    @DisplayName("Invalid login user password mismatch")
    public void invalidLoginPasswordMismatchs(){
        RegisterRequestDTO request = RegisterRequestDTO.builder().email("johndoe@gmail.com").role("ADMIN").password("password").username("John Doe").build();

        authorizationService.register(request);

        Assertions.assertThrows(RuntimeException.class, () -> {authorizationService.login(new LoginRequestDTO("johndoe@gmail.com","passworderronea"));});
    }

    @Test
    @DisplayName("Valid refresh token")
    public void validRefreshToken(){
        RegisterRequestDTO request = RegisterRequestDTO.builder().email("johndoe@gmail.com").role("ADMIN").password("password").username("John Doe").build();

        ResponseTokenDTO tkns = authorizationService.register(request);

        String header = "Bearer ".concat(tkns.getRefreshToken());

        Assertions.assertInstanceOf(ResponseTokenDTO.class, authorizationService.renovateTokens(header));
        Assertions.assertEquals(true, tokenRepository.findByToken(tkns.getRefreshToken()).get().isRevoked());
        Assertions.assertEquals(true, tokenRepository.findByToken(tkns.getRefreshToken()).get().isExpired());
    }

    @Test
    @DisplayName("Invalid refresh token user doesnt exists")
    public void invalidRefreshTokenUserDoesntExists(){
        String header = "Bearer ".concat("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ZjhkNzMwZS05ZTJjLTQxMDMtYTI1MC1mMjFlODY0ZTFlYWUiLCJuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiQURNSU4iLCJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiam9obmRvZUBnbWFpbC5jb20iLCJpYXQiOjE3NTg0OTQ5MzksImV4cCI6MjQ3OTM5NTE2OX0.zIlHc_fozxVLvvy-TBcB6cVNcSGdL4Jn3pPf1ZePXIc");

        Assertions.assertThrows(RuntimeException.class, () -> {authorizationService.renovateTokens(header);});

    }


}

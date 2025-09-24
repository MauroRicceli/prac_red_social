package com.practica_red_social.prac_red_social.configs;

import com.practica_red_social.prac_red_social.exceptions.InvalidTokenType;
import com.practica_red_social.prac_red_social.models.entities.mysql.UserEntity;
import com.practica_red_social.prac_red_social.repositories.TokenRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class AppConfig {

    private final UserRepository userRepository;
    private final EncryptConfig encryptConfig;
    private final TokenRepository tokenRepository;



    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthFilter filterConfig,
            AuthenticationProvider authenticationProvider
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(filterConfig, UsernamePasswordAuthenticationFilter.class)
                // Manejo de excepciones
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"" + authException.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"" + accessDeniedException.getMessage() + "\"}");
                        })
                )
                // Configuración de logout
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                            logout(authHeader);
                        })
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext()
                        )
                );

        return http.build();
    }



    private void logout(String authHeader){
        if(authHeader == null || !authHeader.contains("Bearer ")){
            throw new InvalidTokenType("El token no es de tipo Bearer o es nulo");
        }

        String tkn = authHeader.substring(7);

        tokenRepository.revokeOrExpireToken(tkn);

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            final UserEntity user = userRepository.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getUserRole().toString())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(encryptConfig.obtenerEncriptador());
        return authProvider;
    }

}

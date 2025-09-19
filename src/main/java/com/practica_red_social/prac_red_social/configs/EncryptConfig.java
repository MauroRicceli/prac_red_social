package com.practica_red_social.prac_red_social.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncryptConfig {

    @Bean
    public PasswordEncoder obtenerEncriptador(){
        return new BCryptPasswordEncoder();
    }

}

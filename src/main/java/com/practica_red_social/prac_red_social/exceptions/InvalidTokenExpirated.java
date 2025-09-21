package com.practica_red_social.prac_red_social.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenExpirated extends AuthenticationException {

    public InvalidTokenExpirated(String message){
        super(message);
    }
}

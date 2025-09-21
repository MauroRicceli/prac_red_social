package com.practica_red_social.prac_red_social.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenUserDontExists extends AuthenticationException {

    public InvalidTokenUserDontExists(String message){
        super(message);
    }
}

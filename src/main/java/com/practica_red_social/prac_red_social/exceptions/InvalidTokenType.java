package com.practica_red_social.prac_red_social.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenType extends AuthenticationException {
    public InvalidTokenType(String message){
        super(message);
    }
}

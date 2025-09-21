package com.practica_red_social.prac_red_social.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidHeaderException extends AuthenticationException {

    public InvalidHeaderException(String message){
        super(message);
    }
}

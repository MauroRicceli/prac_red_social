package com.practica_red_social.prac_red_social.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenUserRoleDoesntMatch extends AuthenticationException {

    public InvalidTokenUserRoleDoesntMatch(String message){
        super(message);
    }
}

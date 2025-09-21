package com.practica_red_social.prac_red_social.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenDontExists extends AuthenticationException {

    public InvalidTokenDontExists(String message){super(message);}
}

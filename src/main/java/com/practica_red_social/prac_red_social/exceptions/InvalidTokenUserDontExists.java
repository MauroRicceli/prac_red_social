package com.practica_red_social.prac_red_social.exceptions;

public class InvalidTokenUserDontExists extends RuntimeException{

    public InvalidTokenUserDontExists(String message){
        super(message);
    }
}

package com.practica_red_social.prac_red_social.exceptions;

public class InvalidTokenRevoked extends RuntimeException{

    public InvalidTokenRevoked(String message){
        super(message);
    }
}

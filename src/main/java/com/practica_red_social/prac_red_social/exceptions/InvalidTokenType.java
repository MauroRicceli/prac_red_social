package com.practica_red_social.prac_red_social.exceptions;

public class InvalidTokenType extends RuntimeException{
    public InvalidTokenType(String message){
        super(message);
    }
}

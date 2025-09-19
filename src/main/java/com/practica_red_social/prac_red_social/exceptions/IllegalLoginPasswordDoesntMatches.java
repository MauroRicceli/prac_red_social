package com.practica_red_social.prac_red_social.exceptions;

public class IllegalLoginPasswordDoesntMatches extends RuntimeException{

    public IllegalLoginPasswordDoesntMatches(String message){
        super(message);
    }
}

package com.practica_red_social.prac_red_social.exceptions;

public class IllegalLoginEmailDoesntExists extends RuntimeException{

    public IllegalLoginEmailDoesntExists(String message){
        super(message);
    }
}

package com.practica_red_social.prac_red_social.exceptions;

public class InvalidTokenExpirated extends RuntimeException{

    public InvalidTokenExpirated(String message){
        super(message);
    }
}

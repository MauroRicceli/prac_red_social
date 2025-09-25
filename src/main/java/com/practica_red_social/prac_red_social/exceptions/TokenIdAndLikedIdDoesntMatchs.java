package com.practica_red_social.prac_red_social.exceptions;

public class TokenIdAndLikedIdDoesntMatchs extends RuntimeException{

    public TokenIdAndLikedIdDoesntMatchs(String message){
        super(message);
    }
}

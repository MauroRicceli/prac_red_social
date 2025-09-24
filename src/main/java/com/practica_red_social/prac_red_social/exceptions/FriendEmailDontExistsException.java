package com.practica_red_social.prac_red_social.exceptions;

public class FriendEmailDontExistsException extends RuntimeException{

    public FriendEmailDontExistsException(String message){
        super(message);
    }
}

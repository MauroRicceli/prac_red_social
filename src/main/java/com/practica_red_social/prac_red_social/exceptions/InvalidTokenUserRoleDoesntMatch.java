package com.practica_red_social.prac_red_social.exceptions;

public class InvalidTokenUserRoleDoesntMatch extends RuntimeException{

    public InvalidTokenUserRoleDoesntMatch(String message){
        super(message);
    }
}

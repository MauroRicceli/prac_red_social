package com.practica_red_social.prac_red_social.exceptions;

public class CommentDontExistsInPublication extends RuntimeException{

    public CommentDontExistsInPublication(String message){
        super(message);
    }
}

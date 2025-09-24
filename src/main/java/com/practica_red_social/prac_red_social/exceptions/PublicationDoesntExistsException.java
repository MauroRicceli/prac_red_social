package com.practica_red_social.prac_red_social.exceptions;

public class PublicationDoesntExistsException extends RuntimeException{
    public PublicationDoesntExistsException(String message){
        super(message);
    }
}

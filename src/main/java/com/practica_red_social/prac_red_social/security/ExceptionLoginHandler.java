package com.practica_red_social.prac_red_social.security;

import com.practica_red_social.prac_red_social.exceptions.IllegalLoginEmailDoesntExists;
import com.practica_red_social.prac_red_social.exceptions.IllegalLoginPasswordDoesntMatches;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionLoginHandler {

    @ExceptionHandler(exception = IllegalLoginEmailDoesntExists.class)
    public ResponseEntity<String> handlerIllegalLoginEmailDoesntExists(IllegalLoginEmailDoesntExists e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = IllegalLoginPasswordDoesntMatches.class)
    public ResponseEntity<String> handlerIllegalPasswordDoesntMatches(IllegalLoginPasswordDoesntMatches e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

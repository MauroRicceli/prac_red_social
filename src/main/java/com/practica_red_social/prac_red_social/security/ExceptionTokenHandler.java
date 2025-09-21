package com.practica_red_social.prac_red_social.security;

import com.practica_red_social.prac_red_social.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//TODAS ESTAS EXCEPCIONES OCURREN EN EL FILTRO, POR LO QUE NUNCA LLEGAN AL CONTROLLER.
//PARA EL PROXIMO PROYECTO ARREGLARLO, Y NO HACERLO A NIVEL CONTROLLER SI NO A NIVEL FILTRO.
@ControllerAdvice
public class ExceptionTokenHandler {

    @ExceptionHandler(exception = InvalidTokenUserDontExists.class)
    public ResponseEntity<String> handlerInvalidTokenUserDontExists(InvalidTokenUserDontExists e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidTokenUserRoleDoesntMatch.class)
    public ResponseEntity<String> handlerInvalidTokenUserRoleDoesntMatch(InvalidTokenUserRoleDoesntMatch e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidTokenType.class)
    public ResponseEntity<String> handlerInvalidTokenType(InvalidTokenType e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidTokenDontExists.class)
    public ResponseEntity<String> handlerInvalidTokenDontExists(InvalidTokenDontExists e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidTokenExpirated.class)
    public ResponseEntity<String> handlerInvalidTokenExpirated(InvalidTokenExpirated e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidTokenRevoked.class)
    public ResponseEntity<String> handlerInvalidTokenRevoked(InvalidTokenRevoked e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

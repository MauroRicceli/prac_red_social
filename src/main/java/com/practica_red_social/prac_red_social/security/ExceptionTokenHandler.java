package com.practica_red_social.prac_red_social.security;

import com.practica_red_social.prac_red_social.exceptions.InvalidTokenType;
import com.practica_red_social.prac_red_social.exceptions.InvalidTokenUserDontExists;
import com.practica_red_social.prac_red_social.exceptions.InvalidTokenUserRoleDoesntMatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}

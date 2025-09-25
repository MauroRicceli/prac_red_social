package com.practica_red_social.prac_red_social.security;

import com.practica_red_social.prac_red_social.exceptions.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionUserActivitiesHandler {

    @ExceptionHandler(exception = FriendEmailDontExistsException.class)
    public ResponseEntity<String> handlerFriendEmailDontExists(FriendEmailDontExistsException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = AlreadyFriendsException.class)
    public ResponseEntity<String> handlerAlreadyFriends(AlreadyFriendsException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = NotFriendsAlreadyException.class)
    public ResponseEntity<String> handlerNotFriendsAlready(NotFriendsAlreadyException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = PublicationDoesntExistsException.class)
    public ResponseEntity<String> handlerPublicationDoesntExistsException(PublicationDoesntExistsException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = TokenIdAndLikedIdDoesntMatchs.class)
    public ResponseEntity<String> handlerTokenIdAndLikedIdDoesntMatchs(TokenIdAndLikedIdDoesntMatchs e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = PublicationAlreadyHadTheComment.class)
    public ResponseEntity<String> handlerPublicationAlreadyHadTheComment(PublicationAlreadyHadTheComment e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = CommentDontExistsInPublication.class)
    public ResponseEntity<String> handlerCommentDontExistsInPublication(CommentDontExistsInPublication e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

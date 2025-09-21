package com.practica_red_social.prac_red_social.controllers;

import com.practica_red_social.prac_red_social.models.dtos.LoginRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.LogoutDTO;
import com.practica_red_social.prac_red_social.models.dtos.RegisterRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.ResponseTokenDTO;
import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.services.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthorizationService authorizationService;

    @PutMapping(value="/register",consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseTokenDTO> register(@RequestBody RegisterRequestDTO register){
        return new ResponseEntity<>(authorizationService.register(register), HttpStatus.ACCEPTED);
    }

    @GetMapping(value="/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseTokenDTO> login(@RequestBody LoginRequestDTO loginRequest){
        return new ResponseEntity<>(authorizationService.login(loginRequest), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('STANDARD', 'ADMIN')")
    @GetMapping(value="/renovateTokens", produces = "application/json")
    public ResponseEntity<ResponseTokenDTO> renovateTokens(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        return new ResponseEntity<>(authorizationService.renovateTokens(authHeader), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<LogoutDTO> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        return new ResponseEntity<>(new LogoutDTO(), HttpStatus.ACCEPTED);
    }
}

package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.controllers.AuthController;
import com.practica_red_social.prac_red_social.models.dtos.RegisterRequestDTO;
import com.practica_red_social.prac_red_social.models.dtos.ResponseTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthController authController;

    public ResponseTokenDTO register(RegisterRequestDTO registerRequest){

    }
}

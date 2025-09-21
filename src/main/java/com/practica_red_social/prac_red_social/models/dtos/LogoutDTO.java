package com.practica_red_social.prac_red_social.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LogoutDTO {
    private String message = "El logout fue exitoso";
    private Timestamp time = new Timestamp(System.currentTimeMillis());
}

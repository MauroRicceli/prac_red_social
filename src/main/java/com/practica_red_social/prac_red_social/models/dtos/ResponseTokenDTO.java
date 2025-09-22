package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ResponseTokenDTO {
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;

}

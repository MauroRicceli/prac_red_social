package com.practica_red_social.prac_red_social.models.dtos.auths;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginRequestDTO {
    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min=7, max=50)
    private String password;
}

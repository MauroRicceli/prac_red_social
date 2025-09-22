package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequestDTO {

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min=2, max=25)
    private String username;

    @NotNull
    @Size(min=7, max=50)
    private String password;

    @Enumerated(EnumType.STRING)
    private String role;

}

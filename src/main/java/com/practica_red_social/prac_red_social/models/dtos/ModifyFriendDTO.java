package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ModifyFriendDTO {

    @Email
    @NotNull
    private String email_Amigo;

    private Instant modifyWhen;

}

package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ModifyPublicationDTO {

    @NotNull
    private String idPublication;

    @NotNull
    private String newMessage;

    private Instant whenModified;

}

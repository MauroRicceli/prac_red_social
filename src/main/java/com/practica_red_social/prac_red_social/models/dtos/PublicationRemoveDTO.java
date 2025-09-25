package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class PublicationRemoveDTO {
    @NotNull
    private String idPublication;

    private Instant whenDeleted;
}

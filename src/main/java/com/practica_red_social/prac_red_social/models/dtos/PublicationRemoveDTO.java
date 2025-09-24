package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class PublicationRemoveDTO {
    @NotNull
    private String id_publication;

    private Instant when_deleted;
}

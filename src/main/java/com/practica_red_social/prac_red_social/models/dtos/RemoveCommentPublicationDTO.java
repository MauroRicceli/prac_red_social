package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class RemoveCommentPublicationDTO {

    @NotNull
    private String idComment;

    @NotNull
    private String idPublication;

    private String idCommentToParentCommentInPublication;

    private Instant whenDeleted;
}

package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentPublicationDTO {

    @NotNull
    @Size(min = 0, max = 350)
    private String comment;

    @NotNull
    private String idPublicationRespondedTo;

    private String idCommentOfPublicationRespondedTo;

    private Instant whenCommented;
    private String idGeneratedForThisComment;
}

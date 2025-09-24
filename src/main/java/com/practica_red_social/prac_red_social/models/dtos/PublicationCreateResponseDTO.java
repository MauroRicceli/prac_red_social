package com.practica_red_social.prac_red_social.models.dtos;

import com.practica_red_social.prac_red_social.models.entities.mongodb.Comments;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PublicationCreateResponseDTO {
    @NotNull
    private String id;

    @NotNull
    private String message;

    @Min(0)
    @NotNull
    private int likes;

    @Email
    @NotNull
    private String userEmailDueño;

    @NotNull
    private String usernameDueño;
    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant updatedAt;

    private Set<Comments> comentarios = new HashSet<Comments>();

}

package com.practica_red_social.prac_red_social.models.entities.mongodb;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection="publications")
public class PublicationDocument {
    @Id
    private String id;

    @NotNull
    private String message;

    @Min(0)
    @NotNull
    private Set<Liked> userLiked = new HashSet<Liked>();
    private int likes = 0;

    @Email
    @Indexed
    @NotNull
    private String userEmailDueño;

    @NotNull
    private String usernameDueño;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Set<Comments> comentarios = new HashSet<Comments>();

}

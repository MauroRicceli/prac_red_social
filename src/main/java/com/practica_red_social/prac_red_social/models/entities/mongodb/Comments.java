package com.practica_red_social.prac_red_social.models.entities.mongodb;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comments {

    @NotNull
    @Email
    private String user_email;
    @NotNull
    private String username;

    @Size(min = 1, max = 256)
    private String comment;

    @NotNull
    private List<Comments> replies = new ArrayList<Comments>();

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private Timestamp updatedAt;

    @Min(0)
    private int likes = 0;
}

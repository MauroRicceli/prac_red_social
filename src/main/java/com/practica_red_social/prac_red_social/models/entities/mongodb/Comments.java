package com.practica_red_social.prac_red_social.models.entities.mongodb;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comments {

    @MongoId
    @NotNull
    private String idComentario;

    @NotNull
    @Email
    private String userEmail;
    @NotNull
    private String username;

    @Size(min = 1, max = 256)
    private String comment;

    @NotNull
    private Set<Comments> replies = new HashSet<Comments>();

    private Instant createdAt;

    //se tiene en cuenta en el diseño pero sin funcionalidad real
    @Min(0)
    private int likes = 0;

    @Override
    public boolean equals(Object object){
        if(object instanceof Comments){
            Comments aux = (Comments) object;
            return Objects.equals(aux.getUserEmail(), this.userEmail) && Objects.equals(aux.getUsername(), this.username)
                    && Objects.equals(aux.getComment(), this.comment);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(userEmail, username, comment);
    }
}

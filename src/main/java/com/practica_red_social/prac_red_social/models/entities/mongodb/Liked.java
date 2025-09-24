package com.practica_red_social.prac_red_social.models.entities.mongodb;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Liked {
    @NotNull
    private String userEmailLiked;
    @NotNull
    private String usernameLiked;

    private Instant whenLiked;

    @Override
    public boolean equals(Object object){
        if(object instanceof Liked){
            Liked aux = (Liked) object;
            return Objects.equals(aux.getUsernameLiked(), this.usernameLiked) && Objects.equals(aux.getUserEmailLiked(), this.userEmailLiked);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(userEmailLiked,usernameLiked);
    }
}


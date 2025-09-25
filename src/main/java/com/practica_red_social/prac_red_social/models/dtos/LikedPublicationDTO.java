package com.practica_red_social.prac_red_social.models.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class LikedPublicationDTO {

    public enum Action {
        LIKED, UNLIKED
    }

    @NotNull
    private String idPublication;
    @NotNull
    private String userEmailLiked;
    @NotNull
    private String usernameLiked;

    private Instant whenLiked;

    @Enumerated(EnumType.STRING)
    private Action actionPerformed;
}

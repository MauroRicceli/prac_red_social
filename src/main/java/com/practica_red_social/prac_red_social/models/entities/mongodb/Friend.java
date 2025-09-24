package com.practica_red_social.prac_red_social.models.entities.mongodb;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friend {

    @Email
    @NotNull
    private String emailFriend;

    @NotNull
    private String usernameFriend;

    private Instant dateFriendshipStarted;

    @Override
    public boolean equals(Object object){
        if(object instanceof Friend){
            Friend aux = (Friend) object;
            return Objects.equals(aux.getEmailFriend(), this.emailFriend) && Objects.equals(aux.getUsernameFriend(), this.usernameFriend);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(emailFriend,usernameFriend);
    }

}

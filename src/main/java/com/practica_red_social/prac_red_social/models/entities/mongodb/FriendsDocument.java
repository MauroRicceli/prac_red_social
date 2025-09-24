package com.practica_red_social.prac_red_social.models.entities.mongodb;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection="friendships")
public class FriendsDocument {

    @Id
    private String id;

    @Email
    @NotNull
    @Indexed(unique = true)
    private String userEmailOwner;
    @NotNull
    private String usernameOwner;

    private Set<Friend> friendList = new HashSet<Friend>();

}

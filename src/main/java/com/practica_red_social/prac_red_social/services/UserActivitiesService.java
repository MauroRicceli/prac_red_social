package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.exceptions.AlreadyFriendsException;
import com.practica_red_social.prac_red_social.exceptions.FriendEmailDontExistsException;
import com.practica_red_social.prac_red_social.models.auxiliar.Tupla;
import com.practica_red_social.prac_red_social.models.dtos.AddFriendDTO;
import com.practica_red_social.prac_red_social.models.entities.mongodb.Friend;
import com.practica_red_social.prac_red_social.models.entities.mongodb.FriendsDocument;
import com.practica_red_social.prac_red_social.models.entities.mysql.UserEntity;
import com.practica_red_social.prac_red_social.repositories.FriendshipRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import com.practica_red_social.prac_red_social.services.auths.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserActivitiesService {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    private UserEntity verifyUserExistenceAndGetIt(String email){
        Optional<UserEntity> user = userRepository.getUserByEmail(email);
        if(user.isEmpty()){
            return null;
        }
        return user.get();

    }

    public AddFriendDTO addFriend(String header, AddFriendDTO friendDTO){
        String token = header.substring(7);
        Tupla<String,String> datosUsuarioToken = new Tupla<>(jwtService.extractTokenUsername(token), jwtService.extractTokenNonIdentifierName(token));

        UserEntity newFriend = verifyUserExistenceAndGetIt(friendDTO.getEmail_Amigo());

        if(newFriend == null){
            throw new FriendEmailDontExistsException("El email del usuario a añadir no existe");
        }

        Friend aux = Friend.builder()
                .emailFriend(newFriend.getEmail())
                .usernameFriend(newFriend.getUsername())
                .dateFriendshipStarted(Instant.now())
                .build();

        //EN MONGO NO SE USA OPTIONAL.
        FriendsDocument friendDocument = friendshipRepository.findByUserEmailOwner(datosUsuarioToken.getObjeto1());

        //SI ES EL PRIMER AMIGO DEL USUARIO CREO SU DOCUMENTO.
        if(friendDocument == null){
            Set<Friend> friends = new HashSet<Friend>();
            friends.add(aux);
            FriendsDocument firstFriendsDocument = FriendsDocument.builder()
                    .friendList(friends)
                    .userEmailOwner(datosUsuarioToken.getObjeto1())
                    .usernameOwner(datosUsuarioToken.getObjeto2())
                    .build();
            friendshipRepository.save(firstFriendsDocument);

            friendDTO.setAddedWhen(aux.getDateFriendshipStarted());
            return friendDTO;

        }

        if(!friendDocument.getFriendList().add(aux)){
            throw new AlreadyFriendsException("Los usuarios ya son amigos");
        }

        friendshipRepository.save(friendDocument);

        return friendDTO;
    }
}

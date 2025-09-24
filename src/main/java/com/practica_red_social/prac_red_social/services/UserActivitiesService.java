package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.exceptions.AlreadyFriendsException;
import com.practica_red_social.prac_red_social.exceptions.FriendEmailDontExistsException;
import com.practica_red_social.prac_red_social.exceptions.NotFriendsAlreadyException;
import com.practica_red_social.prac_red_social.exceptions.PublicationDoesntExistsException;
import com.practica_red_social.prac_red_social.models.auxiliar.Tupla;
import com.practica_red_social.prac_red_social.models.dtos.ModifyFriendDTO;
import com.practica_red_social.prac_red_social.models.dtos.PublicationCreateDTO;
import com.practica_red_social.prac_red_social.models.dtos.PublicationRemoveDTO;
import com.practica_red_social.prac_red_social.models.dtos.PublicationCreateResponseDTO;
import com.practica_red_social.prac_red_social.models.entities.mongodb.Friend;
import com.practica_red_social.prac_red_social.models.entities.mongodb.FriendsDocument;
import com.practica_red_social.prac_red_social.models.entities.mongodb.PublicationDocument;
import com.practica_red_social.prac_red_social.models.entities.mysql.UserEntity;
import com.practica_red_social.prac_red_social.repositories.FriendshipRepository;
import com.practica_red_social.prac_red_social.repositories.PublicationRepository;
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
    private final PublicationRepository publicationRepository;

    /**
     * Obtiene el usuario con ese email de la base de datos.
     *
     * @param email
     * @return Null si no existe, UserEntity si sí existe.
     */
    private UserEntity verifyUserExistenceAndGetIt(String email){
        Optional<UserEntity> user = userRepository.getUserByEmail(email);
        if(user.isEmpty()){
            return null;
        }
        return user.get();

    }

    public ModifyFriendDTO addFriend(String header, ModifyFriendDTO friendDTO){
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

            friendDTO.setModifyWhen(aux.getDateFriendshipStarted());
            return friendDTO;

        }

        if(!friendDocument.getFriendList().add(aux)){
            throw new AlreadyFriendsException("Los usuarios ya son amigos");
        }


        friendshipRepository.save(friendDocument);
        friendDTO.setModifyWhen(aux.getDateFriendshipStarted());
        return friendDTO;
    }

    public ModifyFriendDTO removeFriend(String header, ModifyFriendDTO friendDTO){
        String token = header.substring(7);

        UserEntity newFriend = verifyUserExistenceAndGetIt(friendDTO.getEmail_Amigo());

        if(newFriend == null ){
            throw new FriendEmailDontExistsException("El email del usuario a eliminar no existe");
        }

        Friend aux = Friend.builder()
                .emailFriend(newFriend.getEmail())
                .usernameFriend(newFriend.getUsername())
                .dateFriendshipStarted(Instant.now())
                .build();

        FriendsDocument friends = friendshipRepository.findByUserEmailOwner(jwtService.extractTokenUsername(token));

        if(friends == null || !friends.getFriendList().remove(aux)){
            throw new NotFriendsAlreadyException("Los usuarios no son amigos");
        }

        friendshipRepository.save(friends);

        friendDTO.setModifyWhen(aux.getDateFriendshipStarted());
        return friendDTO;

    }

    public PublicationCreateResponseDTO createPublication(String auth, PublicationCreateDTO publicationDTO){
        String token = auth.substring(7);
        Tupla<String, String> datosUsuario = new Tupla<String,String>(jwtService.extractTokenUsername(token), jwtService.extractTokenNonIdentifierName(token));

        PublicationDocument publication = PublicationDocument.builder()
                .message(publicationDTO.getMessage())
                .userEmailDueño(datosUsuario.getObjeto1())
                .usernameDueño(datosUsuario.getObjeto2())
                .build();

        publicationRepository.save(publication);

        return PublicationCreateResponseDTO.builder()
                .likes(publication.getLikes())
                .createdAt(publication.getCreatedAt())
                .comentarios(publication.getComentarios())
                .message(publicationDTO.getMessage())
                .updatedAt(publication.getUpdatedAt())
                .userEmailDueño(publication.getUserEmailDueño())
                .usernameDueño(publication.getUsernameDueño())
                .id(publication.getId())
                .build();
    }

    public PublicationRemoveDTO removePublication(String auth, PublicationRemoveDTO publicationRemoveDTO){
        String token = auth.substring(7);

        long publicationsDeleted = publicationRepository.deleteByid(publicationRemoveDTO.getId_publication());

        if(publicationsDeleted <= 0){
            throw new PublicationDoesntExistsException("La publicación que quiere eliminar no existe");
        }

        publicationRemoveDTO.setWhen_deleted(Instant.now());
        return publicationRemoveDTO;
    }
}

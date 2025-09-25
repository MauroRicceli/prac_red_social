package com.practica_red_social.prac_red_social.services;

import com.practica_red_social.prac_red_social.exceptions.AlreadyFriendsException;
import com.practica_red_social.prac_red_social.exceptions.FriendEmailDontExistsException;
import com.practica_red_social.prac_red_social.exceptions.NotFriendsAlreadyException;
import com.practica_red_social.prac_red_social.exceptions.PublicationDoesntExistsException;
import com.practica_red_social.prac_red_social.models.auxiliar.Tupla;
import com.practica_red_social.prac_red_social.models.dtos.*;
import com.practica_red_social.prac_red_social.models.entities.mongodb.Friend;
import com.practica_red_social.prac_red_social.models.entities.mongodb.FriendsDocument;
import com.practica_red_social.prac_red_social.models.entities.mongodb.Liked;
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

    /**
     * Verifica si la publiacion existe por ID
     *
     * @param id de la publicacion
     * @throws PublicationDoesntExistsException si la publicacion que se quiere verificar y obtener no existe
     * @return PublicationDocument si existe, PublicationDoesntExistsException si no.
     */
    private PublicationDocument verifyPublicationExistenceAndGetIt(String id){
        return publicationRepository.findById(id).orElseThrow(() -> new PublicationDoesntExistsException("La publicacion no existe"));
    }

    /**
     * Agrega un nuevo amigo al usuario poseedor del token siempre que no lo sean anteriormente entre ellos y el usuario exista.
     * Si es su primer amigo crea el documento.
     * @param header
     * @param friendDTO
     * @throws FriendEmailDontExistsException si el usuario que se quiere añadir de amigo no existe
     * @throws AlreadyFriendsException si los usuarios ya son amigos
     * @return ModifyFriendDTO con informacion
     */
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

    /**
     * Elimina al amigo del poseedor del token siempre que exista y sean amigos.
     * @param header
     * @param friendDTO
     * @throws FriendEmailDontExistsException si el email del usuario que se quiere eliminar la amistad no existe
     * @throws NotFriendsAlreadyException si los usuarios no son amigos entre sí y se intentan eliminar
     * @return ModifyFriendDTO con información util
     */
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

    /**
     * Crea una publicacion para el usuario poseedor del token
     * @param auth
     * @param publicationDTO
     * @return PublicationCreateResponseDTO con informacion util
     */
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

    /**
     * Elimina la publicacion enviada siempre y cuando exista y sea de propiedad del usuario enviado por el token
     * @param auth
     * @param publicationRemoveDTO
     * @throws PublicationDoesntExistsException si no existe la publicacion que se quiere borrar, o si no es de su propiedad
     * @return PublicationRemoveDTO con información util
     */
    public PublicationRemoveDTO removePublication(String auth, PublicationRemoveDTO publicationRemoveDTO){
        String token = auth.substring(7);

        long publicationsDeleted = publicationRepository.deleteByIdAndUserEmailDueño(publicationRemoveDTO.getIdPublication(), jwtService.extractTokenUsername(token));

        if(publicationsDeleted <= 0){
            throw new PublicationDoesntExistsException("La publicación que quiere eliminar no existe o no es del usuario solicitante");
        }

        publicationRemoveDTO.setWhenDeleted(Instant.now());
        return publicationRemoveDTO;
    }

    public ModifyPublicationDTO modifyPublication(String auth, ModifyPublicationDTO modifiedPublicationDTO){

        String token = auth.substring(7);

        PublicationDocument publication = verifyPublicationExistenceAndGetIt(modifiedPublicationDTO.getIdPublication());


        if(!publication.getUserEmailDueño().equals(jwtService.extractTokenUsername(token))){
            throw new PublicationDoesntExistsException("La publicacion no pertenece a ese usuario");
        }

        publication.setMessage(modifiedPublicationDTO.getNewMessage());

        publicationRepository.save(publication);

        modifiedPublicationDTO.setWhenModified(publication.getUpdatedAt());
        return modifiedPublicationDTO;
    }

    public LikedPublicationDTO manageLikedPublication(LikedPublicationDTO likedPublicationDTO){

        PublicationDocument publication = verifyPublicationExistenceAndGetIt(likedPublicationDTO.getIdPublication());

        Liked nuevoLike = Liked.builder()
                .userEmailLiked(likedPublicationDTO.getUserEmailLiked())
                .usernameLiked(likedPublicationDTO.getUsernameLiked())
                .whenLiked(Instant.now())
                .build();

        if(!publication.getUserLiked().add(nuevoLike)) {
             publication.getUserLiked().remove(nuevoLike);
             publication.setLikes(publication.getUserLiked().size());
             publicationRepository.save(publication);
             likedPublicationDTO.setActionPerformed(LikedPublicationDTO.Action.UNLIKED);
             likedPublicationDTO.setWhenLiked(nuevoLike.getWhenLiked());
             return likedPublicationDTO;
        }

        publication.setLikes(publication.getUserLiked().size());
        publicationRepository.save(publication);
        likedPublicationDTO.setActionPerformed(LikedPublicationDTO.Action.LIKED);
        likedPublicationDTO.setWhenLiked(nuevoLike.getWhenLiked());
        return likedPublicationDTO;

    }
}

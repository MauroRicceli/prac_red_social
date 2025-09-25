package com.practica_red_social.prac_red_social.servicesTests.unitTests;

import com.practica_red_social.prac_red_social.exceptions.AlreadyFriendsException;
import com.practica_red_social.prac_red_social.exceptions.FriendEmailDontExistsException;
import com.practica_red_social.prac_red_social.exceptions.NotFriendsAlreadyException;
import com.practica_red_social.prac_red_social.exceptions.PublicationDoesntExistsException;
import com.practica_red_social.prac_red_social.models.dtos.*;
import com.practica_red_social.prac_red_social.models.entities.mongodb.Friend;
import com.practica_red_social.prac_red_social.models.entities.mongodb.FriendsDocument;
import com.practica_red_social.prac_red_social.models.entities.mongodb.Liked;
import com.practica_red_social.prac_red_social.models.entities.mongodb.PublicationDocument;
import com.practica_red_social.prac_red_social.models.entities.mysql.UserEntity;
import com.practica_red_social.prac_red_social.repositories.FriendshipRepository;
import com.practica_red_social.prac_red_social.repositories.PublicationRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import com.practica_red_social.prac_red_social.services.UserActivitiesService;
import com.practica_red_social.prac_red_social.services.auths.JWTService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserActivitiesServiceTest {

    @InjectMocks
    private UserActivitiesService userActivitiesService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private JWTService jwtService;

    @Test
    @DisplayName("Test add first friend valid")
    public void testAddFirstFriendValid(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(jwtService.extractTokenNonIdentifierName("tokendeprueba")).thenReturn("testOwner");
        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.of(userEntity));
        Mockito.when(friendshipRepository.findByUserEmailOwner("testOwner@gmail.com")).thenReturn(null);


        ModifyFriendDTO ret = userActivitiesService.addFriend("Bearer tokendeprueba", friendDTO);
        friendDTO.setModifyWhen(ret.getModifyWhen());

        Assertions.assertEquals(ret,friendDTO);
    }

    @Test
    @DisplayName("Test add friend valid")
    public void testAddFriendValid(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();
        FriendsDocument friendDocument = FriendsDocument.builder().userEmailOwner("test@gmail.com").usernameOwner("test").friendList(new HashSet<Friend>()).build();

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(jwtService.extractTokenNonIdentifierName("tokendeprueba")).thenReturn("testOwner");
        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.of(userEntity));
        Mockito.when(friendshipRepository.findByUserEmailOwner("testOwner@gmail.com")).thenReturn(friendDocument);


        ModifyFriendDTO ret = userActivitiesService.addFriend("Bearer tokendeprueba", friendDTO);
        friendDTO.setModifyWhen(ret.getModifyWhen());

        Assertions.assertEquals(ret,friendDTO);
    }

    @Test
    @DisplayName("Test add existing friend invalid")
    public void testAddExistingFriend(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();
        FriendsDocument friendDocument = FriendsDocument.builder().userEmailOwner("test@gmail.com").usernameOwner("test").friendList(new HashSet<Friend>()).build();
        Friend friend = Friend.builder().emailFriend("test@gmail.com").usernameFriend("test").dateFriendshipStarted(Instant.now()).build();

        friendDocument.getFriendList().add(friend);


        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(jwtService.extractTokenNonIdentifierName("tokendeprueba")).thenReturn("testOwner");
        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.of(userEntity));
        Mockito.when(friendshipRepository.findByUserEmailOwner("testOwner@gmail.com")).thenReturn(friendDocument);

        Assertions.assertThrows(AlreadyFriendsException.class, () -> {userActivitiesService.addFriend("Bearer tokendeprueba", friendDTO);});
    }

    @Test
    @DisplayName("Test add friend invalid dont exists")
    public void testAddUnexistingFriend(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(jwtService.extractTokenNonIdentifierName("tokendeprueba")).thenReturn("testOwner");
        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(FriendEmailDontExistsException.class, () -> {userActivitiesService.addFriend("Bearer tokendeprueba", friendDTO);});
    }

    @Test
    @DisplayName("Test remove friend valid")
    public void testRemoveFriend(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();
        FriendsDocument friendDocument = FriendsDocument.builder().userEmailOwner("test@gmail.com").usernameOwner("test").friendList(new HashSet<Friend>()).build();
        Friend friend = Friend.builder().emailFriend("test@gmail.com").usernameFriend("test").dateFriendshipStarted(Instant.now()).build();
        friendDocument.getFriendList().add(friend);

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.of(userEntity));
        Mockito.when(friendshipRepository.findByUserEmailOwner("testOwner@gmail.com")).thenReturn(friendDocument);


        ModifyFriendDTO ret = userActivitiesService.removeFriend("Bearer tokendeprueba", friendDTO);
        friendDTO.setModifyWhen(ret.getModifyWhen());
        Assertions.assertEquals(ret, friendDTO);
    }

    @Test
    @DisplayName("Test remove friend invalid, Friend dont exists")
    public void testRemoveFriendDontExists(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();

        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(FriendEmailDontExistsException.class, () -> {userActivitiesService.removeFriend("Bearer tokendeprueba", friendDTO);});
    }

    @Test
    @DisplayName("Test remove friend invalid, Document is null")
    public void testRemoveFriendDocumentNull(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.of(userEntity));
        Mockito.when(friendshipRepository.findByUserEmailOwner("testOwner@gmail.com")).thenReturn(null);

        Assertions.assertThrows(NotFriendsAlreadyException.class, () -> {userActivitiesService.removeFriend("Bearer tokendeprueba", friendDTO);});
    }

    @Test
    @DisplayName("Test remove friend invalid, not friends")
    public void testRemoveFriendNotFriends(){
        ModifyFriendDTO friendDTO = new ModifyFriendDTO("test@gmail.com", null);
        UserEntity userEntity = UserEntity.builder().email("test@gmail.com").username("test").build();
        FriendsDocument friendDocument = FriendsDocument.builder().userEmailOwner("test@gmail.com").usernameOwner("test").friendList(new HashSet<Friend>()).build();

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(userRepository.getUserByEmail("test@gmail.com")).thenReturn(Optional.of(userEntity));
        Mockito.when(friendshipRepository.findByUserEmailOwner("testOwner@gmail.com")).thenReturn(friendDocument);

        Assertions.assertThrows(NotFriendsAlreadyException.class, () -> {userActivitiesService.removeFriend("Bearer tokendeprueba", friendDTO);});
    }

    @Test
    @DisplayName("Test publication create valid")
    public void testPublicationCreate(){
        PublicationCreateDTO publicationCreateDTO = new PublicationCreateDTO("mensaje de prueba");

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(jwtService.extractTokenNonIdentifierName("tokendeprueba")).thenReturn("testOwner");

        PublicationCreateResponseDTO ret = userActivitiesService.createPublication("Bearer tokendeprueba", publicationCreateDTO);

        PublicationCreateResponseDTO test = PublicationCreateResponseDTO.builder()
                .createdAt(ret.getCreatedAt())
                .updatedAt(ret.getUpdatedAt())
                .id(ret.getId())
                .comentarios(ret.getComentarios())
                .likes(ret.getLikes())
                .message("mensaje de prueba")
                .userEmailDueño("testOwner@gmail.com")
                .usernameDueño("testOwner")
                .build();

        Assertions.assertEquals(ret, test);
    }

    @Test
    @DisplayName("Test remove publication valid")
    public void testRemovePublication(){
        PublicationRemoveDTO publicationRemoveDTO = new PublicationRemoveDTO("idDePrueba", null);

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(publicationRepository.deleteByIdAndUserEmailDueño(publicationRemoveDTO.getIdPublication(), "testOwner@gmail.com")).thenReturn(1L);

        PublicationRemoveDTO ret = userActivitiesService.removePublication("Bearer tokendeprueba", publicationRemoveDTO);

        publicationRemoveDTO.setWhenDeleted(ret.getWhenDeleted());

        Assertions.assertEquals(ret, publicationRemoveDTO);

    }

    @Test
    @DisplayName("Test remove publication invalid, dont exists")
    public void testRemovePublicationDontExists(){
        PublicationRemoveDTO publicationRemoveDTO = new PublicationRemoveDTO("idDePrueba", null);

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(publicationRepository.deleteByIdAndUserEmailDueño(publicationRemoveDTO.getIdPublication(), "testOwner@gmail.com")).thenReturn(0L);

        Assertions.assertThrows(PublicationDoesntExistsException.class, () -> {userActivitiesService.removePublication("Bearer tokendeprueba", publicationRemoveDTO);});
    }

    @Test
    @DisplayName("Test modify publication valid")
    public void testModifyPublication(){
        ModifyPublicationDTO modifyPublicationDTO = new ModifyPublicationDTO("idDePrueba","mensaje de prueba",null);
        PublicationDocument publication = PublicationDocument.builder()
                .id("idDePrueba")
                .message("mensaje antiguo")
                .userEmailDueño("testOwner@gmail.com")
                .build();

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(publicationRepository.findById("idDePrueba")).thenReturn(Optional.of(publication));

        ModifyPublicationDTO ret = userActivitiesService.modifyPublication("Bearer tokendeprueba", modifyPublicationDTO);

        modifyPublicationDTO.setWhenModified(ret.getWhenModified());

        Assertions.assertEquals(ret, modifyPublicationDTO);

    }

    @Test
    @DisplayName("Test modify publication invalid, publication dont exists")
    public void testModifyPublicationDontExist(){
        ModifyPublicationDTO modifyPublicationDTO = new ModifyPublicationDTO("idDePrueba","mensaje de prueba",null);

        Mockito.when(publicationRepository.findById("idDePrueba")).thenReturn(Optional.empty());

        Assertions.assertThrows(PublicationDoesntExistsException.class, () -> {userActivitiesService.modifyPublication("Bearer tokendeprueba", modifyPublicationDTO);});
    }

    @Test
    @DisplayName("Test modify publication invalid, publication not owner")
    public void testModifyPublicationNotOwner(){
        ModifyPublicationDTO modifyPublicationDTO = new ModifyPublicationDTO("idDePrueba","mensaje de prueba",null);
        PublicationDocument publication = PublicationDocument.builder()
                .id("idDePrueba")
                .message("mensaje antiguo")
                .userEmailDueño("testinvalido@gmail.com")
                .build();

        Mockito.when(jwtService.extractTokenUsername("tokendeprueba")).thenReturn("testOwner@gmail.com");
        Mockito.when(publicationRepository.findById("idDePrueba")).thenReturn(Optional.of(publication));

        Assertions.assertThrows(PublicationDoesntExistsException.class, () -> {userActivitiesService.modifyPublication("Bearer tokendeprueba", modifyPublicationDTO);});
    }

    @Test
    @DisplayName("Test manage liked publication valid, LIKE")
    public void testManageLikedPublication(){
        LikedPublicationDTO likedPublicationDTO = LikedPublicationDTO.builder()
                .idPublication("idDePrueba")
                .usernameLiked("testLike")
                .userEmailLiked("testLike@gmail.com")
                .actionPerformed(LikedPublicationDTO.Action.LIKED)
                .build();

        PublicationDocument publication = PublicationDocument.builder()
                .id("idDePrueba")
                .message("mensaje antiguo")
                .userEmailDueño("pubOwner@gmail.com")
                .userLiked(new HashSet<Liked>())
                .likes(0)
                .build();

        Mockito.when(publicationRepository.findById("idDePrueba")).thenReturn(Optional.of(publication));

        LikedPublicationDTO ret = userActivitiesService.manageLikedPublication(likedPublicationDTO);

        likedPublicationDTO.setWhenLiked(ret.getWhenLiked());

        Assertions.assertEquals(ret, likedPublicationDTO);

    }

    @Test
    @DisplayName("Test manage liked publication valid, UNLIKE")
    public void testManageUnlikedPublication(){
        LikedPublicationDTO likedPublicationDTO = LikedPublicationDTO.builder()
                .idPublication("idDePrueba")
                .usernameLiked("testLike")
                .userEmailLiked("testLike@gmail.com")
                .actionPerformed(LikedPublicationDTO.Action.UNLIKED)
                .build();

        PublicationDocument publication = PublicationDocument.builder()
                .id("idDePrueba")
                .message("mensaje antiguo")
                .userEmailDueño("pubOwner@gmail.com")
                .userLiked(new HashSet<Liked>())
                .likes(0)
                .build();

        publication.getUserLiked().add(new Liked("testLike@gmail.com", "testLike", Instant.now()));

        Mockito.when(publicationRepository.findById("idDePrueba")).thenReturn(Optional.of(publication));

        LikedPublicationDTO ret = userActivitiesService.manageLikedPublication(likedPublicationDTO);

        likedPublicationDTO.setWhenLiked(ret.getWhenLiked());

        Assertions.assertEquals(ret, likedPublicationDTO);

    }
}

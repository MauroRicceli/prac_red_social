package com.practica_red_social.prac_red_social.servicesTests.integrationTests;

import com.practica_red_social.prac_red_social.exceptions.*;
import com.practica_red_social.prac_red_social.models.dtos.*;
import com.practica_red_social.prac_red_social.models.entities.mysql.UserEntity;
import com.practica_red_social.prac_red_social.repositories.FriendshipRepository;
import com.practica_red_social.prac_red_social.repositories.TokenRepository;
import com.practica_red_social.prac_red_social.repositories.UserRepository;
import com.practica_red_social.prac_red_social.services.UserActivitiesService;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class UserActivitiesServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserActivitiesService userActivitiesService;
    @Autowired
    private TokenRepository tokenRepository;


    private final String tokenDeAccesoTest = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ZjhkNzMwZS05ZTJjLTQxMDMtYTI1MC1mMjFlODY0ZTFlYWUiLCJuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiQURNSU4iLCJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiam9obmRvZUBnbWFpbC5jb20iLCJpYXQiOjE3NTg0OTQ5MzksImV4cCI6MjQ3OTM5NTE2OX0.zIlHc_fozxVLvvy-TBcB6cVNcSGdL4Jn3pPf1ZePXIc";
    private final String tokenDeRefrescoTest = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyYjAyZmE3Mi01NTAzLTQzZTMtOGMyOC0yMmE5ZDFlZGI5ZWEiLCJuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiQURNSU4iLCJ0eXBlIjoicmVmcmVzaCIsInN1YiI6ImpvaG5kb2VAZ21haWwuY29tIiwiaWF0IjoxNzU4NDk0OTM5LCJleHAiOjI0Mzk4MDg5NTE2OX0.YN2bELt4vszsFbMmcyaXyxthJiBLNYkDFFdSRNkAZ-8";
    private final String headerAccessTkn = "Bearer ".concat(tokenDeAccesoTest);

    @BeforeAll
    public static void firstSetup(){
        //CARGO MIS VARIABLES DE ENTORNO.
        Path envPath = Paths.get(".env.test");
        if (Files.exists(envPath)) {
            Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        }
    }

    @BeforeEach
    public void setupEachTest(){
        tokenRepository.deleteAll();
        userRepository.deleteAll();
        friendshipRepository.deleteAll();
    }

    /**
     * <b>John Doe</b> <code>johndoe@gmail.com</code>  <i>(token owner)<i> <br>
     *
     * <b>Mary Poppins</b> <code>marypoppins@gmail.com </code>
     */
    public void generateTwoUsersInDBForTest(){
        UserEntity usrMain = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();
        UserEntity usrFriend = UserEntity.builder().username("Mary Poppins").email("marypoppins@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("STANDARD")).build();

        userRepository.save(usrMain);
        userRepository.save(usrFriend);
    }

    /**
     * <b>John Doe</b> <code>johndoe@gmail.com</code>  <i>(token owner)<i> <br>
     */
    private void generateJustOneUserInDBForTest(){
        UserEntity usrMain = UserEntity.builder().username("John Doe").email("johndoe@gmail.com").password("password").userRole(UserEntity.UserRole.valueOf("ADMIN")).build();
        userRepository.save(usrMain);
    }

    @Test
    @DisplayName("Test add friend valid")
    public void addFriendTest(){
        generateTwoUsersInDBForTest();

        ModifyFriendDTO modifyFriendDTO = new ModifyFriendDTO("marypoppins@gmail.com",null);

        ModifyFriendDTO ret = userActivitiesService.addFriend(headerAccessTkn, modifyFriendDTO);
        modifyFriendDTO.setModifyWhen(ret.getModifyWhen());

        Assertions.assertEquals(ret, modifyFriendDTO);

    }

    @Test
    @DisplayName("Test add friend invalid, already friends")
    public void addFriendAlreadyFriends(){
        generateTwoUsersInDBForTest();

        ModifyFriendDTO modifyFriendDTO = new ModifyFriendDTO("marypoppins@gmail.com",null);

        userActivitiesService.addFriend(headerAccessTkn, modifyFriendDTO);

        Assertions.assertThrows(AlreadyFriendsException.class, () -> {userActivitiesService.addFriend(headerAccessTkn, modifyFriendDTO);});

    }

    @Test
    @DisplayName("Test add friend invalid, friend dont exists")
    public void addFriendDontExists(){
        generateJustOneUserInDBForTest();

        ModifyFriendDTO modifyFriendDTO = new ModifyFriendDTO("marypoppins@gmail.com",null);

        Assertions.assertThrows(FriendEmailDontExistsException.class, () -> {userActivitiesService.addFriend(headerAccessTkn, modifyFriendDTO);});

    }

    @Test
    @DisplayName("Test remove friend valid")
    public void removeFriendTest(){
        generateTwoUsersInDBForTest();

        ModifyFriendDTO modifyFriendDTO = new ModifyFriendDTO("marypoppins@gmail.com",null);

        userActivitiesService.addFriend(headerAccessTkn, modifyFriendDTO);

        ModifyFriendDTO ret = userActivitiesService.removeFriend(headerAccessTkn, modifyFriendDTO);
        modifyFriendDTO.setModifyWhen(ret.getModifyWhen());

        Assertions.assertEquals(ret, modifyFriendDTO);

    }

    @Test
    @DisplayName("Test remove friend not exists")
    public void removeFriendNotExists(){
        generateJustOneUserInDBForTest();

        ModifyFriendDTO modifyFriendDTO = new ModifyFriendDTO("marypoppins@gmail.com",null);

        Assertions.assertThrows(FriendEmailDontExistsException.class, () -> {userActivitiesService.removeFriend(headerAccessTkn, modifyFriendDTO);});
    }

    @Test
    @DisplayName("Test remove friend, not friends")
    public void removeFriendNotFriends(){
        generateTwoUsersInDBForTest();

        ModifyFriendDTO modifyFriendDTO = new ModifyFriendDTO("marypoppins@gmail.com",null);

        Assertions.assertThrows(NotFriendsAlreadyException.class, () -> {userActivitiesService.removeFriend(headerAccessTkn, modifyFriendDTO);});
    }

    @Test
    @DisplayName("Test create publication valid")
    public void createPublicationTest(){
        generateJustOneUserInDBForTest();

        PublicationCreateDTO publicationTest = new PublicationCreateDTO("test message");

        PublicationCreateResponseDTO ret = userActivitiesService.createPublication(headerAccessTkn, publicationTest);

        Assertions.assertEquals("test message", ret.getMessage());
        Assertions.assertEquals("johndoe@gmail.com", ret.getUserEmailDueño());
        Assertions.assertEquals("John Doe", ret.getUsernameDueño());

    }

    @Test
    @DisplayName("Test remove publication valid")
    public void removePublicationTest(){
        generateJustOneUserInDBForTest();

        PublicationCreateDTO publicationTest = new PublicationCreateDTO("test message");

        PublicationCreateResponseDTO aux = userActivitiesService.createPublication(headerAccessTkn, publicationTest);

        PublicationRemoveDTO ret = userActivitiesService.removePublication(headerAccessTkn, new PublicationRemoveDTO(aux.getId(), null));

        Assertions.assertEquals(ret.getIdPublication(), aux.getId());

    }

    @Test
    @DisplayName("Test remove publication invalid, dont exists")
    public void removePublicationNotExists(){
        generateJustOneUserInDBForTest();

        PublicationCreateDTO publicationTest = new PublicationCreateDTO("test message");

        Assertions.assertThrows(PublicationDoesntExistsException.class, () -> {userActivitiesService.removePublication(headerAccessTkn, new PublicationRemoveDTO("idtesteo", null));});

    }

    @Test
    @DisplayName("Test modify publication valid")
    public void modifyPublicationTest(){
        generateJustOneUserInDBForTest();

        PublicationCreateDTO publicationTest = new PublicationCreateDTO("test message");

        PublicationCreateResponseDTO aux = userActivitiesService.createPublication(headerAccessTkn, publicationTest);

        ModifyPublicationDTO ret = userActivitiesService.modifyPublication(headerAccessTkn, new ModifyPublicationDTO(aux.getId(),"mensaje modificado", null));

        Assertions.assertEquals("mensaje modificado", ret.getNewMessage());
        Assertions.assertEquals(aux.getId(), ret.getIdPublication());

    }

    @Test
    @DisplayName("Test manage liked publication valid, LIKED")
    public void manageLikedPublicationLikedTest(){
        generateJustOneUserInDBForTest();
        PublicationCreateDTO publicationTest = new PublicationCreateDTO("test message");
        PublicationCreateResponseDTO aux = userActivitiesService.createPublication(headerAccessTkn, publicationTest);

        LikedPublicationDTO ret = userActivitiesService.manageLikedPublication(headerAccessTkn, new LikedPublicationDTO(aux.getId(), "johndoe@gmail.com", "John Doe", null, null));

        Assertions.assertEquals(ret.getIdPublication(), aux.getId());
        Assertions.assertEquals("johndoe@gmail.com", ret.getUserEmailLiked());
        Assertions.assertEquals("John Doe", ret.getUsernameLiked());
        Assertions.assertEquals(LikedPublicationDTO.Action.LIKED, ret.getActionPerformed());
    }

    @Test
    @DisplayName("Test manage liked publication valid, UNLIKED")
    public void manageLikedPublicationUnlikedTest(){
        generateJustOneUserInDBForTest();
        PublicationCreateDTO publicationTest = new PublicationCreateDTO("test message");
        PublicationCreateResponseDTO aux = userActivitiesService.createPublication(headerAccessTkn, publicationTest);

        userActivitiesService.manageLikedPublication(headerAccessTkn, new LikedPublicationDTO(aux.getId(), "johndoe@gmail.com", "John Doe", null, null));
        LikedPublicationDTO ret = userActivitiesService.manageLikedPublication(headerAccessTkn, new LikedPublicationDTO(aux.getId(), "johndoe@gmail.com", "John Doe", null, null));

        Assertions.assertEquals(ret.getIdPublication(), aux.getId());
        Assertions.assertEquals("johndoe@gmail.com", ret.getUserEmailLiked());
        Assertions.assertEquals("John Doe", ret.getUsernameLiked());
        Assertions.assertEquals(LikedPublicationDTO.Action.UNLIKED, ret.getActionPerformed());
    }

    @Test
    @DisplayName("Test manage liked publication unvalid, token id and user doesnt match")
    public void manageLikedPublicationTokenAndUserDoesntMatch(){
        generateJustOneUserInDBForTest();
        PublicationCreateDTO publicationTest = new PublicationCreateDTO("test message");
        PublicationCreateResponseDTO aux = userActivitiesService.createPublication(headerAccessTkn, publicationTest);

        Assertions.assertThrows(TokenIdAndLikedIdDoesntMatchs.class, () -> {userActivitiesService.manageLikedPublication(headerAccessTkn, new LikedPublicationDTO(aux.getId(), "marypoppins@gmail.com", "John Doe", null, null));;});

    }

    @Test
    @DisplayName("Test manage liked publication unvalid, publication doesnt exists")
    public void manageLikedPublicationDoesntExists(){
        generateJustOneUserInDBForTest();

        Assertions.assertThrows(PublicationDoesntExistsException.class, () -> {userActivitiesService.manageLikedPublication(headerAccessTkn, new LikedPublicationDTO("test", "johndoe@gmail.com", "John Doe", null, null));;});

    }







}

package com.practica_red_social.prac_red_social.controllers;

import com.practica_red_social.prac_red_social.models.dtos.*;
import com.practica_red_social.prac_red_social.services.UserActivitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserActivitiesController {

    private final UserActivitiesService userActivitiesService;

    @PreAuthorize("hasAnyRole('STANDARD', 'ADMIN')")
    @PutMapping(value = "/addFriend", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ModifyFriendDTO> addFriend(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody ModifyFriendDTO friendDTO){
        return new ResponseEntity<>(userActivitiesService.addFriend(auth,friendDTO), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('STANDARD', 'ADMIN')")
    @PutMapping(value="/removeFriend", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ModifyFriendDTO> removeFriend(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody ModifyFriendDTO friendDTO){
        return new ResponseEntity<>(userActivitiesService.removeFriend(auth,friendDTO), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('STANDARD', 'ADMIN')")
    @PutMapping(value="/createPublication", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PublicationCreateResponseDTO> createPublication(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody PublicationCreateDTO publicationDTO){
        return new ResponseEntity<>(userActivitiesService.createPublication(auth, publicationDTO), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('STANDARD', 'ADMIN')")
    @PutMapping(value="/removePublication", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PublicationRemoveDTO> removePublication(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody PublicationRemoveDTO publicationRemoveDTO){
        return new ResponseEntity<>(userActivitiesService.removePublication(auth, publicationRemoveDTO), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('STANDARD', 'ADMIN')")
    @PutMapping(value="/modifyPublication", consumes="application/json", produces="application/json")
    public ResponseEntity<ModifyPublicationDTO> modifyPublication(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@RequestBody ModifyPublicationDTO modifiedPublicationDTO){
        return new ResponseEntity<>(userActivitiesService.modifyPublication(auth, modifiedPublicationDTO), HttpStatus.ACCEPTED);
    }

    /**
     * Si la publicacion ya tenia un like lo quita, y si no lo tenia lo agrega.
     * @param auth
     * @param likedPublicationDTO
     * @return DTO con info or exception si ocurre
     */
    @PreAuthorize("hasAnyRole('STANDARD', 'ADMIN')")
    @PutMapping(value="/likePublication", consumes="application/json", produces = "application/json")
    public ResponseEntity<LikedPublicationDTO> likePublication(@RequestHeader(HttpHeaders.AUTHORIZATION)String auth, @RequestBody LikedPublicationDTO likedPublicationDTO){
        return new ResponseEntity<>(userActivitiesService.manageLikedPublication(auth, likedPublicationDTO), HttpStatus.ACCEPTED);
    }
}

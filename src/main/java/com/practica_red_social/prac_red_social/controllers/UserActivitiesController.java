package com.practica_red_social.prac_red_social.controllers;

import com.practica_red_social.prac_red_social.models.dtos.ModifyFriendDTO;
import com.practica_red_social.prac_red_social.models.dtos.PublicationCreateDTO;
import com.practica_red_social.prac_red_social.models.dtos.PublicationRemoveDTO;
import com.practica_red_social.prac_red_social.models.dtos.PublicationCreateResponseDTO;
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
}

package com.practica_red_social.prac_red_social.controllers;

import com.practica_red_social.prac_red_social.models.dtos.AddFriendDTO;
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
    public ResponseEntity<AddFriendDTO> addFriend(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody AddFriendDTO friendDTO){
        return new ResponseEntity<>(userActivitiesService.addFriend(auth,friendDTO), HttpStatus.ACCEPTED);
    }
}

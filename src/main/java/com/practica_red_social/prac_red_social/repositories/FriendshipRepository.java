package com.practica_red_social.prac_red_social.repositories;

import com.practica_red_social.prac_red_social.models.entities.mongodb.FriendsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends MongoRepository<FriendsDocument, String> {

    FriendsDocument findByUserEmailOwner(String emailOwner);
}

package com.practica_red_social.prac_red_social.repositories;

import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> getUserByEmail(String email);
}

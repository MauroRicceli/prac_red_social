package com.practica_red_social.prac_red_social.repositories.testsrepos;

import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepositoryTests extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getUserByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.userRole =:newrole WHERE u.email =:email")
    void updateRole(@Param("email") String email, @Param("newrole") UserEntity.UserRole role);
}

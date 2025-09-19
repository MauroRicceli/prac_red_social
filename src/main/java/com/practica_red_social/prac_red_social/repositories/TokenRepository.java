package com.practica_red_social.prac_red_social.repositories;

import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query("SELECT t FROM TokenEntity t WHERE t.usuarioPertenece =:usuarioPertenece")
    Optional<TokenEntity> findByUser(@Param("usuarioPertenece")UserEntity user);
}

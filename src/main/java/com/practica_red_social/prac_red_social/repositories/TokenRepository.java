package com.practica_red_social.prac_red_social.repositories;

import com.practica_red_social.prac_red_social.models.entities.TokenEntity;
import com.practica_red_social.prac_red_social.models.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query("SELECT t FROM TokenEntity t WHERE t.usuarioPertenece =:usuarioPertenece")
    Optional<TokenEntity> findByUser(@Param("usuarioPertenece")UserEntity user);

    @Query("SELECT t FROM TokenEntity t WHERE t.usuarioPertenece.email =:usuarioPerteneceEmail")
    Optional<TokenEntity> findByUserEmail(@Param("usuarioPerteneceEmail") String emailUser);

    Optional<TokenEntity> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE TokenEntity t SET t.revoked=true, t.expired=true WHERE t.usuarioPertenece =:user")
    void revokeOrExpireTokenByUser(@Param("user") UserEntity user);

    @Modifying
    @Transactional
    @Query("UPDATE TokenEntity t SET t.revoked=true, t.expired=true WHERE t.token =:token")
    void revokeOrExpireToken(@Param("token") String token);

    @Modifying
    @Transactional
    @Query("UPDATE TokenEntity t SET t.revoked=true, t.expired=true WHERE t.usuarioPertenece.email =:userEmail")
    void revokeOrExpireTokenByUserEmail(@Param("userEmail") String userEmail);

}

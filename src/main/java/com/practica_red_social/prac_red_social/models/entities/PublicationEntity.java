package com.practica_red_social.prac_red_social.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="publications")
public class PublicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String message;

    @Min(0)
    @NotNull
    private int likes = 0;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userPertenece;

    @Column(nullable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    //PARA CUANDO LO HAGA DE VERDAD A LA RED SOCIAL, HACER TODA LA PARTE DE PUBLICACIONES EN NOSQL, MONGODB.

}

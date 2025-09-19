package com.practica_red_social.prac_red_social.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="usuarios")
public class UserEntity {

    public enum UserRole{
        ADMIN, STANDARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min=2, max = 25)
    private String username;

    @NotNull
    private String password;

    @Email
    @NotNull
    private String email;

    @Min(16)
    private int edad;

    @NotNull
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}

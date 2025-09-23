package com.practica_red_social.prac_red_social.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Fetch;

import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_posted", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    @Size(min = 1, max = 256)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_to", nullable = true)
    private CommentsEntity comment_responded_to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "on_publication",nullable = false)
    private PublicationEntity publication;

    @Column(nullable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private Timestamp updatedAt;

    @Column(nullable = false)
    @Min(0)
    private int likes = 0;
}

package com.mycodingtest.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`USER`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String picture;
    private String oauthProvider;
    private String oauthId;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public User(String name, String email, String picture, String oauthProvider, String oauthId) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
    }
}

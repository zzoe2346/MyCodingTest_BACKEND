package com.mycodingtest.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
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

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }
}

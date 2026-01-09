package com.mycodingtest.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final Long userId;
    @Getter
    private final String picture;
    private final String username;
    private final String password = null;
    private final Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE"));

    public CustomUserDetails(Long userId, String picture, String username) {
        this.userId = userId;
        this.picture = picture;
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}

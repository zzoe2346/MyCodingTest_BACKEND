package com.mycodingtest.infra.user;

import com.mycodingtest.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        return User.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                .oauthId(entity.getOauthId())
                .oauthProvider(entity.getOauthProvider())
                .picture(entity.getPicture())
                .build();
    }

    public UserEntity toEntity(User domain) {
        return UserEntity.builder()
                .email(domain.getEmail())
                .name(domain.getName())
                .oauthId(domain.getOauthId())
                .oauthProvider(domain.getOauthProvider())
                .picture(domain.getPicture())
                .build();
    }
}

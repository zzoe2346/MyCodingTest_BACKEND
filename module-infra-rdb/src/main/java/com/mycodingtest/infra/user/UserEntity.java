package com.mycodingtest.infra.user;

import com.mycodingtest.domain.user.User;
import com.mycodingtest.infra.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "`user`")
public class UserEntity extends BaseEntity {

    private String name;
    private String email;
    private String picture;
    private String oauthProvider;
    private String oauthId;

    public static UserEntity from(User domain) {
        return UserEntity.builder()
                .name(domain.getName())
                .email(domain.getEmail())
                .picture(domain.getPicture())
                .oauthProvider(domain.getOauthProvider())
                .oauthId(domain.getOauthId())
                .build();
    }

    public User toDomain() {
        return User.from(
                getId(),
                this.name,
                this.email,
                this.picture,
                this.oauthProvider,
                this.oauthId);
    }

}
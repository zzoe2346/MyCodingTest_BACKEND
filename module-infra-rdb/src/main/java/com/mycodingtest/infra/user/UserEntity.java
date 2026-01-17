package com.mycodingtest.infra.user;

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

}
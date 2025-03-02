package com.mycodingtest.user;

import com.mycodingtest.user.dto.UserDetailInfoResoponse;

public class UserMapper {
    public static UserDetailInfoResoponse toDetailInfoResponse(User entity) {
        return new UserDetailInfoResoponse(
                entity.getId(),
                entity.getName(),
                entity.getPicture(),
                entity.getOauthProvider(),
                entity.getOauthId(),
                entity.getCreatedAt()
        );
    }
}

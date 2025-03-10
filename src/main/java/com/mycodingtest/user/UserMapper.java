package com.mycodingtest.user;

import com.mycodingtest.user.dto.UserDetailInfoResoponse;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

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

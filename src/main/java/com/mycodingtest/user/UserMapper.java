package com.mycodingtest.user;

import com.mycodingtest.user.dto.UserDetailInfoResponse;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static UserDetailInfoResponse toDetailInfoResponse(User entity) {
        return new UserDetailInfoResponse(
                entity.getId(),
                entity.getName(),
                entity.getPicture(),
                entity.getOauthProvider(),
                entity.getOauthId(),
                entity.getCreatedAt()
        );
    }
}

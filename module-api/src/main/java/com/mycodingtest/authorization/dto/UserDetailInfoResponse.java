package com.mycodingtest.authorization.dto;

import java.time.LocalDateTime;

public record UserDetailInfoResponse(
        Long id,
        String name,
        String picture,
        String oauthProvider,
        String oauthId,
        LocalDateTime createdAt
) {
}

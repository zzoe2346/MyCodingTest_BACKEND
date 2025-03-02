package com.mycodingtest.user.dto;

import java.time.LocalDateTime;

public record UserDetailInfoResoponse(
        Long id,
        String name,
        String picture,
        String oauthProvider,
        String oauthId,
        LocalDateTime createdAt
) {
}

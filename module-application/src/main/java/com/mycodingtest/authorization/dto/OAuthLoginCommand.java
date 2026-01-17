package com.mycodingtest.authorization.dto;

public record OAuthLoginCommand(
         String email,
         String name,
         String picture,
         String oauthId,
         String provider
) {
}

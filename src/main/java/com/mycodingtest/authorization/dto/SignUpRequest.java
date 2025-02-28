package com.mycodingtest.authorization.dto;

public record SignUpRequest(
        String username,
        String password,
        String email
) {
}

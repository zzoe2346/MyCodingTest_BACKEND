package com.mycodingtest.authorization;

public record SignUpRequest(
        String username,
        String password,
        String email
) {
}

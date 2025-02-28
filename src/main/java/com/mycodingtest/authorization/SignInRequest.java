package com.mycodingtest.authorization;

public record SignInRequest(
        String username,
        String password
) {
}

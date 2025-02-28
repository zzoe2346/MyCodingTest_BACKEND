package com.mycodingtest.authorization.dto;

public record SignInRequest(
        String username,
        String password
) {
}

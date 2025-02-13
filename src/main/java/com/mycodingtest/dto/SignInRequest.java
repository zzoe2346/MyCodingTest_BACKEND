package com.mycodingtest.dto;

public record SignInRequest(
        String username,
        String password
) {
}

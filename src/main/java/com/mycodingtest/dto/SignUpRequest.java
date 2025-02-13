package com.mycodingtest.dto;

public record SignUpRequest(
        String username,
        String password,
        String email
) {
}

package com.mycodingtest.dto;

public record VerifyCodeRequest(
        String email,
        String code
) {
}

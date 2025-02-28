package com.mycodingtest.review.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewStatusUpdateRequest(
        @NotNull
        boolean reviewed
) {
}

package com.mycodingtest.review;

import jakarta.validation.constraints.NotNull;

public record ReviewStatusUpdateRequest(
        @NotNull
        boolean reviewed
) {
}

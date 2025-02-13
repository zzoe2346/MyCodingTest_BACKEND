package com.mycodingtest.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewStatusUpdateRequest(
        @NotNull
        boolean reviewed
) {
}

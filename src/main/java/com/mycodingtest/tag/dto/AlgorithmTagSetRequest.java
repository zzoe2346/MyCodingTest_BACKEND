package com.mycodingtest.tag.dto;

import jakarta.validation.constraints.Size;

public record AlgorithmTagSetRequest(
        @Size(max = 7)
        int[] tagIds
) {
}

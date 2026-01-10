package com.mycodingtest.deprecated.solvedproblemtag.dto;

import jakarta.validation.constraints.Size;

public record AlgorithmTagSetRequest(
        @Size(max = 7)
        int[] tagIds
) {
}

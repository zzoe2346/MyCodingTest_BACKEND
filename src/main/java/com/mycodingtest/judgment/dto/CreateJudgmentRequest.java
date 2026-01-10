package com.mycodingtest.judgment.dto;

import java.time.LocalDateTime;

public record CreateJudgmentRequest(
        Long submissionId,
        String baekjoonId,
        int problemNumber,
        String problemTitle,
        String resultText,
        int memory,
        int time,
        String language,
        int codeLength,
        LocalDateTime submittedAt
) {
}

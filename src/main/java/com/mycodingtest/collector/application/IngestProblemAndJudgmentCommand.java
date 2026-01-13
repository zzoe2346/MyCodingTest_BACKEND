package com.mycodingtest.collector.application;

import com.mycodingtest.judgment.application.dto.CreateBojJudgmentCommand;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IngestProblemAndJudgmentCommand(
        Integer problemNumber,
        String problemTitle,
        Long submissionId,
        String baekjoonId,
        String resultText,
        int memory,
        int time,
        String language,
        int codeLength,
        LocalDateTime submittedAt,
        String sourceCode,
        Long userId
) {
}

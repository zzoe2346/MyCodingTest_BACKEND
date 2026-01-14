package com.mycodingtest.review.application.dto;

import com.mycodingtest.collector.application.IngestProblemAndJudgmentCommand;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateReviewCommand(
        Long problemId,
        Long userId,
        String sourceCode,
        LocalDateTime submittedAt,
        String resultText
) {
    public static CreateReviewCommand from(IngestProblemAndJudgmentCommand command, Long problemId) {
        return CreateReviewCommand.builder()
                .problemId(problemId)
                .userId(command.userId())
                .sourceCode(command.sourceCode())
                .submittedAt(command.submittedAt())
                .resultText(command.resultText())
                .build();
    }
}

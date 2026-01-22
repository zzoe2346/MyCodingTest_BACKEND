package com.mycodingtest.application.review.command;

 import com.mycodingtest.application.collector.command.CreateProblemAndJudgmentCommand;
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
    public static CreateReviewCommand from(CreateProblemAndJudgmentCommand command, Long problemId) {
        return CreateReviewCommand.builder()
                .problemId(problemId)
                .userId(command.userId())
                .sourceCode(command.sourceCode())
                .submittedAt(command.submittedAt())
                .resultText(command.resultText())
                .build();
    }
}

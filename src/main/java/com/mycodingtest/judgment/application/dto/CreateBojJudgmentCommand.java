package com.mycodingtest.judgment.application.dto;

import com.mycodingtest.collector.application.IngestProblemAndJudgmentCommand;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateBojJudgmentCommand(
        Long submissionId,
        String baekjoonId,
        String resultText,
        int memory,
        int time,
        String language,
        int codeLength,
        LocalDateTime submittedAt,
        String sourceCode,
        Long problemId,
        Long userId
) {
    public static CreateBojJudgmentCommand from(IngestProblemAndJudgmentCommand command, Long problemId) {
        return CreateBojJudgmentCommand.builder()
                .submissionId(command.submissionId())
                .baekjoonId(command.baekjoonId())
                .resultText(command.resultText())
                .memory(command.memory())
                .time(command.time())
                .language(command.language())
                .codeLength(command.codeLength())
                .submittedAt(command.submittedAt())
                .sourceCode(command.sourceCode())
                .problemId(problemId)
                .userId(command.userId())
                .build();
    }
}

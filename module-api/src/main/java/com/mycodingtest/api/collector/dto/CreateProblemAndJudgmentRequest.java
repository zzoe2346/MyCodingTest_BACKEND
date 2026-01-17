package com.mycodingtest.api.collector.dto;

import com.mycodingtest.application.collector.dto.CreateProblemAndJudgmentCommand;

import java.time.LocalDateTime;

public record CreateProblemAndJudgmentRequest(
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
        String code
) {

    public CreateProblemAndJudgmentCommand toCommand(Long userId) {
        return CreateProblemAndJudgmentCommand.builder()
                .problemNumber(problemNumber)
                .problemTitle(problemTitle)
                .submissionId(submissionId)
                .baekjoonId(baekjoonId)
                .resultText(resultText)
                .memory(memory)
                .time(time)
                .language(language)
                .codeLength(codeLength)
                .submittedAt(submittedAt)
                .sourceCode(code)
                .userId(userId)
                .build();
    }

}
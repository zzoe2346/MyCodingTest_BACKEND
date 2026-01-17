package com.mycodingtest.collector.dto;

import com.mycodingtest.application.collector.IngestProblemAndJudgmentCommand;

import java.time.LocalDateTime;

public record IngestProblemAndJudgmentRequest(
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

    public IngestProblemAndJudgmentCommand toCommand(Long userId) {
        return IngestProblemAndJudgmentCommand.builder()
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
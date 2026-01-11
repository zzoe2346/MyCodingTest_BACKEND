package com.mycodingtest.collector.api.dto;

import com.mycodingtest.collector.application.RegisterBojSolutionCommand;

import java.time.LocalDateTime;

public record CreateBojProblemAndJudgmentRequest(
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

    public RegisterBojSolutionCommand toCommand() {
        return RegisterBojSolutionCommand.builder()
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
                .build();
    }

}
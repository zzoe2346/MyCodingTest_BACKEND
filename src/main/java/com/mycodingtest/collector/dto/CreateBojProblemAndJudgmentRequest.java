package com.mycodingtest.collector.dto;

import com.mycodingtest.collector.application.CreateProblemAndJudgmentFromBojCommand;

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
        LocalDateTime submittedAt
) {

    public CreateProblemAndJudgmentFromBojCommand toCommand() {
        return new CreateProblemAndJudgmentFromBojCommand(
                problemNumber,
                problemTitle,
                submissionId,
                baekjoonId,
                resultText,
                memory,
                time,
                language,
                codeLength,
                submittedAt
        );
    }

}

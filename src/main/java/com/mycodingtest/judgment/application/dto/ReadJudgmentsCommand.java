package com.mycodingtest.judgment.application.dto;

public record ReadJudgmentsCommand(
        Long problemId,
        Long userId
) {
    public static ReadJudgmentsCommand from(Long problemId, Long userId) {
        return new ReadJudgmentsCommand(problemId, userId);
    }
}

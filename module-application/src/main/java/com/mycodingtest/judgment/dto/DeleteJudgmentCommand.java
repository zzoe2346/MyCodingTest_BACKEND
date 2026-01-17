package com.mycodingtest.judgment.dto;

public record DeleteJudgmentCommand(
        Long judgmentId,
        Long userId
) {
    public static DeleteJudgmentCommand from(Long judgmentId, Long userId) {
        return new DeleteJudgmentCommand(judgmentId, userId);
    }
}

package com.mycodingtest.application.judgment.command;

public record DeleteJudgmentCommand(
        Long judgmentId,
        Long userId
) {
    public static DeleteJudgmentCommand from(Long judgmentId, Long userId) {
        return new DeleteJudgmentCommand(judgmentId, userId);
    }
}

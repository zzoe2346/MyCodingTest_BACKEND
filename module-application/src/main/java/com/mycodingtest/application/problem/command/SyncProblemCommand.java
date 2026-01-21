package com.mycodingtest.application.problem.command;

import com.mycodingtest.application.collector.dto.CreateProblemAndJudgmentCommand;
import com.mycodingtest.domain.common.Platform;

public record SyncProblemCommand(
        Integer problemNumber,
        String problemTitle,
        Platform platform
) {
    public static SyncProblemCommand from(CreateProblemAndJudgmentCommand command, Platform platform) {
        return new SyncProblemCommand(command.problemNumber(), command.problemTitle(), platform);
    }
}
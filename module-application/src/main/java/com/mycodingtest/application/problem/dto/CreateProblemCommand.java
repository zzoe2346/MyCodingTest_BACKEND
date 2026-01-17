package com.mycodingtest.application.problem.dto;

 import com.mycodingtest.application.collector.CreateProblemAndJudgmentCommand;
 import com.mycodingtest.domain.common.Platform;

public record CreateProblemCommand(
        Integer problemNumber,
        String problemTitle,
        Platform platform
) {
    public static CreateProblemCommand from(CreateProblemAndJudgmentCommand command, Platform platform) {
        return new CreateProblemCommand(command.problemNumber(), command.problemTitle(), platform);
    }
}
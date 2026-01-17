package com.mycodingtest.problem.dto;

 import com.mycodingtest.collector.IngestProblemAndJudgmentCommand;
 import com.mycodingtest.domain.common.Platform;

public record CreateProblemCommand(
        Integer problemNumber,
        String problemTitle,
        Platform platform
) {
    public static CreateProblemCommand from(IngestProblemAndJudgmentCommand command, Platform platform) {
        return new CreateProblemCommand(command.problemNumber(), command.problemTitle(), platform);
    }
}
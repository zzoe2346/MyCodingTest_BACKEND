package com.mycodingtest.problem.application.dto;

import com.mycodingtest.collector.application.RegisterBojSolutionCommand;
import com.mycodingtest.common.domain.Platform;

public record CreateProblemCommand(
        Integer problemNumber,
        String problemTitle,
        Platform platform
) {
    public static CreateProblemCommand from(RegisterBojSolutionCommand command, Platform platform) {
        return new CreateProblemCommand(command.problemNumber(), command.problemTitle(), platform);
    }
}
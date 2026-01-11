package com.mycodingtest.judgment.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RegisterBojJudgmentCommand {
    private final Long submissionId;
    private final String baekjoonId;
    private final String resultText;
    private final int memory;
    private final int time;
    private final String language;
    private final int codeLength;
    private final LocalDateTime submittedAt;
    private final String sourceCode;
}

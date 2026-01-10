package com.mycodingtest.collector.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateProblemAndJudgmentFromBojCommand {
    Integer problemNumber;
    String problemTitle;
    Long submissionId;
    String baekjoonId;
    String resultText;
    int memory;
    int time;
    String language;
    int codeLength;
    LocalDateTime submittedAt;
}

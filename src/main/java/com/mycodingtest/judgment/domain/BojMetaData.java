package com.mycodingtest.judgment.domain;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class BojMetaData implements MetaData {
    Long submissionId;
    String baekjoonId;
    String resultText;
    int memory;
    int time;
    String language;
    int codeLength;
    LocalDateTime submittedAt;
}

package com.mycodingtest.judgment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
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

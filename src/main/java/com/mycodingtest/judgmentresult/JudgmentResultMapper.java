package com.mycodingtest.judgmentresult;

import com.mycodingtest.judgmentresult.dto.JudgmentResultResponse;

public class JudgmentResultMapper {

    public static JudgmentResultResponse toResponse(JudgmentResult entity) {
        return new JudgmentResultResponse(
                entity.getSubmissionId(),
                entity.getBaekjoonId(),
                entity.getProblemId(),
                entity.getResultText(),
                entity.getMemory(),
                entity.getTime(),
                entity.getLanguage(),
                entity.getCodeLength(),
                entity.getSubmittedAt(),
                entity.getId()
        );
    }
}

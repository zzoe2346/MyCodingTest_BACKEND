package com.mycodingtest.domain.common;

import com.mycodingtest.domain.judgment.JudgmentStatus;

/**
 * <h3>코딩 테스트 플랫폼 (Platform)</h3>
 * <p>
 * 시스템에서 지원하는 알고리즘 문제 풀이 플랫폼을 정의합니다.
 * 각 플랫폼은 고유한 문제 번호 체계와 메타데이터 형식을 가집니다.
 * </p>
 */
public enum Platform {
    BOJ {
        @Override
        public JudgmentStatus toStatus(String resultText) {
            if (resultText.contains("맞았습니다")) return JudgmentStatus.SUCCESS;
            if (resultText.contains("틀렸습니다")) return JudgmentStatus.FAIL;
            return JudgmentStatus.ERROR;
        }
    },
    Programmers {
        @Override
        public JudgmentStatus toStatus(String resultText) {
            return null;
        }
    };

    public abstract JudgmentStatus toStatus(String resultText);

}

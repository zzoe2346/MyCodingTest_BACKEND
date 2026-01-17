package com.mycodingtest.domain.judgment;

import com.mycodingtest.domain.common.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Judgment {
    private Long id;

    private Long problemId;

    /**
     * 채점을 수행한 사용자의 식별자
     */
    private Long userId;

    /**
     * <b>외부 플랫폼의 제출 ID (Unique Key Candidate)</b>
     * <p>
     * 백준(BOJ) 등의 플랫폼에서 부여한 고유 제출 번호입니다.
     * 동일한 제출 내역이 중복으로 수집되는 것을 방지하기 위한 식별자로 사용됩니다.
     * </p>
     */
    private Long submissionId;

    /**
     * 채점 결과 상태 (성공, 실패 등)
     */
    private JudgmentStatus status;

    /**
     * 채점이 이루어진 원본 플랫폼 정보
     */
    private Platform platform;

    /**
     * <b>플랫폼별 특화 메타데이터 (JSON)</b>
     * <p>
     * 플랫폼마다 상이한 채점 정보(메모리, 시간, 언어 버전 등)를 유연하게 저장하기 위해 반정규화된 JSON 컬럼을 사용합니다.
     * JPA 컨버터를 통해 {@link MetaData} 인터페이스 구현체로 매핑됩니다.
     * </p>
     */
    private MetaData metaData;

    /**
     * 채점한 소스 코드
     */
    private String sourceCode;

    public static JudgmentStatus getJudgmentStatus(String resultText) {
        return JudgmentStatus.SUCCESS;//임시
    }

}

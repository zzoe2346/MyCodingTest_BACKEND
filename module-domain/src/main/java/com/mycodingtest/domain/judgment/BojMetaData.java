package com.mycodingtest.domain.judgment;

import com.mycodingtest.domain.common.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h3>백준 채점 메타데이터 (BojMetaData)</h3>
 * <p>
 * 백준 온라인 저지(BOJ) 플랫폼에 특화된 채점 상세 정보를 담는 Value Object입니다.
 * {@link MetaData} 인터페이스를 구현하며, {@link Judgment} 엔티티의 JSON 컬럼에 저장됩니다.
 * </p>
 */
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BojMetaData implements MetaData {
    /**
     * 플랫폼에서의 제출 번호
     */
    Long submissionId;

    /**
     * 제출한 사용자의 백준 아이디
     */
    String baekjoonId;

    /**
     * 채점 결과 텍스트 (예: "맞았습니다!!", "시간 초과")
     */
    String resultText;

    /**
     * 사용 메모리 (KB 단위)
     */
    int memory;

    /**
     * 실행 시간 (ms 단위)
     */
    int time;

    /**
     * 제출 언어 (예: "Java 11", "Python 3")
     */
    String language;

    /**
     * 코드 길이 (byte 단위)
     */
    int codeLength;

    /**
     * 제출 일시
     */
    LocalDateTime submittedAt;
}
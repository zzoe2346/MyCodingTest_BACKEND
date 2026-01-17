package com.mycodingtest.domain.judgment;

import com.mycodingtest.domain.common.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * <h3>채점 (Judgment)</h3>
 * <p>
 * 사용자가 특정 플랫폼(BOJ, Programmers 등)에서 문제를 풀고 제출한 개별 채점 결과를 나타내는 Aggregate Root입니다.
 * 하나의 문제는 여러 번의 채점 기록을 가질 수 있으며, 본 시스템은 이 채점 기록을 수집하여 리뷰 데이터를 생성하는 기초 자료로 활용합니다.
 * </p>
 */
@Getter
@AllArgsConstructor
@Builder
public class Judgment {

    /**
     * 고유 식별자
     */
    private Long id;

    /**
     * 채점된 문제의 식별자
     */
    private Long problemId;

    /**
     * 채점을 수행한 사용자의 식별자
     */
    private Long userId;

    /**
     * <b>외부 플랫폼의 제출 식별자</b>
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
     * 플랫폼별 특화 메타데이터
     * <p>
     * 플랫폼마다 상이한 채점 정보(메모리, 시간, 언어 버전 등)를 유연하게 저장하기 위해 반정규화된 JSON 형태로 관리합니다.
     */
    private MetaData metaData;

    /**
     * 채점한 소스 코드
     */
    private String sourceCode;

}

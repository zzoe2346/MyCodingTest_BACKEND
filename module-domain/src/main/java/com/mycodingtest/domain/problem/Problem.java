package com.mycodingtest.domain.problem;

import com.mycodingtest.domain.common.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Builder
@Getter
public class Problem {
    private Long id;
    /**
     * 문제 번호 (예: 백준 1000번)
     */
    private Integer problemNumber;

    /**
     * 문제 제목
     */
    private String problemTitle;

    /**
     * 출처 플랫폼 (BOJ, PROGRAMMERS 등)
     */
    private Platform platform;

}

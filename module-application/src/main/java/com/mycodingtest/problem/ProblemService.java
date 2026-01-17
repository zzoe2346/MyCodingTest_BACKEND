package com.mycodingtest.problem;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import com.mycodingtest.problem.dto.CreateProblemCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <h3>문제 데이터 서비스 (ProblemService)</h3>
 * <p>
 * 플랫폼별 알고리즘 문제 정보를 관리합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    /**
     * <b>문제 정보 동기화</b>
     * <p>
     * 전달받은 문제 번호와 플랫폼 정보를 기준으로 DB를 검색하여
     * 이미 존재하면 기존 정보를 반환하고, 없으면 새로 생성하여 저장합니다.
     * </p>
     */
    public Problem getOrCreateProblem(CreateProblemCommand command) {
        return problemRepository.findProblem(command.problemNumber(), command.platform())
                .orElseGet(() -> problemRepository.save(
                                Problem.builder()
                                        .problemNumber(command.problemNumber())
                                        .problemTitle(command.problemTitle()).platform(Platform.BOJ)
                                        .build()
                        )
                );
    }

}
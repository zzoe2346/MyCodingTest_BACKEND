package com.mycodingtest.application.problem.command;

import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>문제 데이터 서비스 (ProblemCommandService)</h3>
 * <p>
 * 플랫폼별 알고리즘 문제 정보를 관리합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ProblemCommandService {

    private final ProblemRepository problemRepository;

    /**
     * <b>문제 정보 동기화</b>
     * <p>
     * 전달받은 문제 번호와 플랫폼 정보를 기준으로 DB를 검색하여
     * 이미 존재하면 기존 정보를 반환하고, 없으면 새로 생성하여 저장합니다.
     * </p>
     */
    @Transactional
    public Long syncProblem(SyncProblemCommand command) {
        return problemRepository.findProblemByproblemNumberAndPlatform(command.problemNumber(), command.platform())
                .orElseGet(() -> problemRepository.save(
                        Problem.from(
                                command.problemNumber(),
                                command.problemTitle(),
                                command.platform())))
                .getId();
    }

}
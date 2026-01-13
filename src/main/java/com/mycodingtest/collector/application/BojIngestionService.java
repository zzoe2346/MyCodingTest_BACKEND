package com.mycodingtest.collector.application;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.judgment.application.JudgmentService;
import com.mycodingtest.judgment.application.dto.CreateBojJudgmentCommand;
import com.mycodingtest.problem.application.ProblemService;
import com.mycodingtest.problem.application.dto.CreateProblemCommand;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.review.application.ReviewService;
import com.mycodingtest.review.application.dto.CreateReviewCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <h3>백준 데이터 수집 유스케이스 (BojIngestionService)</h3>
 * <p>
 * 크롬 익스텐션 등 클라이언트로부터 전달받은 백준 풀이 정보를 시스템에 통합하는 오케스트레이션 서비스입니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class BojIngestionService {

    private final JudgmentService judgmentService;
    private final ProblemService problemService;
    private final ReviewService reviewService;

    /**
     * <b>데이터 통합(Ingestion) 프로세스</b>
     * <ol>
     *     <li>문제 정보가 DB에 없으면 새로 생성, 있으면 조회합니다.</li>
     *     <li>플랫폼 고유의 상세 채점 기록(Judgment)을 저장합니다.</li>
     *     <li>사용자가 학습을 기록할 수 있도록 리뷰(Review) 대기 상태를 생성합니다.</li>
     * </ol>
     */
    @Transactional
    public void ingest(IngestProblemAndJudgmentCommand command) {
        // 1. 문제 엔티티 확보
        Problem problem = problemService.getOrCreateProblem(CreateProblemCommand.from(command, Platform.BOJ));
        // 2. 채점 상세 기록 저장
        judgmentService.createJudgmentFromBoj(CreateBojJudgmentCommand.from(command, problem.getId()));
        // 3. 리뷰 오답 노트 생성
        reviewService.createReview(CreateReviewCommand.from(command, problem.getId()));
    }

    /**
     * 중복 수집 방지를 위해 외부 제출 번호의 존재 여부를 체크합니다.
     *
     * @throws IllegalStateException 이미 수집된 제출 번호인 경우
     */
    public void checkDuplicatedSubmissionId(Long submissionId) {
        boolean isDuplicated = judgmentService.isJudgmentExist(submissionId, Platform.BOJ);
        if (isDuplicated) {
            throw new IllegalStateException("이미 존재하는 제출 번호입니다: " + submissionId);
        }
    }

}
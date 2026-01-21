package com.mycodingtest.application.collector;

import com.mycodingtest.application.collector.dto.CreateProblemAndJudgmentCommand;
import com.mycodingtest.application.judgment.command.CreateBojJudgmentCommand;
import com.mycodingtest.application.judgment.command.JudgmentCommandService;
import com.mycodingtest.application.judgment.query.JudgmentQueryService;
import com.mycodingtest.application.problem.command.ProblemCommandService;
import com.mycodingtest.application.problem.command.SyncProblemCommand;
import com.mycodingtest.application.review.command.CreateReviewCommand;
import com.mycodingtest.application.review.command.ReviewCommandService;
import com.mycodingtest.domain.common.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>백준 데이터 수집 유스케이스 (BojIngestionService)</h3>
 * <p>
 * 크롬 익스텐션 등 클라이언트로부터 전달받은 백준 풀이 정보를 시스템에 통합하는 오케스트레이션 서비스입니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class BojIngestionService {

    private final JudgmentCommandService judgmentCommandService;
    private final JudgmentQueryService judgmentQueryService;
    private final ProblemCommandService problemCommandService;
    private final ReviewCommandService reviewCommandService;

    /**
     * <b>데이터 통합(Ingestion) 프로세스</b>
     * <ol>
     *     <li>문제 정보가 DB에 없으면 새로 생성, 있으면 조회합니다.</li>
     *     <li>플랫폼 고유의 상세 채점 기록(Judgment)을 저장합니다.</li>
     *     <li>사용자가 학습을 기록할 수 있도록 리뷰(Review) 대기 상태를 생성합니다.</li>
     * </ol>
     */
    @Transactional
    public void ingest(CreateProblemAndJudgmentCommand command) {
        // 1. 문제 엔티티 확보
        Long syncedProblemId = problemCommandService.syncProblem(SyncProblemCommand.from(command, Platform.BOJ));
        // 2. 채점 상세 기록 저장
        judgmentCommandService.createJudgmentFromBoj(CreateBojJudgmentCommand.from(command, syncedProblemId));
        // 3. 리뷰 오답 노트 생성
        reviewCommandService.createReview(CreateReviewCommand.from(command, syncedProblemId));
    }

    /**
     * 중복 수집 방지를 위해 외부 제출 번호의 존재 여부를 체크합니다.
     *
     * @throws IllegalStateException 이미 수집된 제출 번호인 경우
     */
    public void checkDuplicatedSubmissionId(Long submissionId) {
        boolean isDuplicated = judgmentQueryService.isJudgmentExist(submissionId, Platform.BOJ);
        if (isDuplicated) {
            throw new IllegalStateException("이미 존재하는 제출 번호입니다: " + submissionId);
        }
    }

}
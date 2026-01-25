package com.mycodingtest.application.user.command;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import com.mycodingtest.domain.review.ReviewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <h3>환영 문제 서비스 (WelcomeProblemService)</h3>
 * <p>
 * 신규 사용자를 위한 환영 문제와 리뷰를 생성하는 서비스입니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class WelcomeService {

    private static final Integer WELCOME_PROBLEM_NUMBER = 0;
    private static final String WELCOME_PROBLEM_TITLE = "HELLO WORLD";
    private static final String WELCOME_CODE = """
            // Welcome to MyCodingTest! 🎉
            // 환영합니다!
            //
            // 이 서비스는 알고리즘 문제 풀이를 복습하고
            // 학습 진도를 추적하는 데 도움을 드립니다.
            //
            // 시작해볼까요?
            
            public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println("Hello, World!");
                }
            }
            """;
    private static final String WELCOME_RESULT = "환영합니다! 🎉";

    private final ProblemRepository problemRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 신규 사용자를 위한 환영 문제와 IN_PROGRESS 상태의 리뷰를 생성합니다.
     *
     * @param userId 신규 사용자 ID
     */
    @Transactional
    public void createWelcomeProblemForUser(Long userId) {
        Problem welcomeProblem = getOrCreateWelcomeProblem();
        createWelcomeReview(welcomeProblem.getId(), userId);
    }

    private Problem getOrCreateWelcomeProblem() {
        return problemRepository.findProblemByproblemNumberAndPlatform(WELCOME_PROBLEM_NUMBER, Platform.WELCOME)
                .orElseGet(() -> problemRepository.save(
                        Problem.from(WELCOME_PROBLEM_NUMBER, WELCOME_PROBLEM_TITLE, Platform.WELCOME)));
    }

    private void createWelcomeReview(Long problemId, Long userId) {
        Review welcomeReview = Review.builder()
                .problemId(problemId)
                .userId(userId)
                .revisedCode(WELCOME_CODE)
                .recentSubmitAt(LocalDateTime.now())
                .recentResult(WELCOME_RESULT)
                .status(ReviewStatus.TO_DO)
                .reviewed(false)
                .favorited(false)
                .build();
        reviewRepository.create(welcomeReview);
    }
}

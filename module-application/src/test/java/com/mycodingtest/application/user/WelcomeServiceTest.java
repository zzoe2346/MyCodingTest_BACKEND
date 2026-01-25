package com.mycodingtest.application.user;

import com.mycodingtest.application.user.command.WelcomeService;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import com.mycodingtest.domain.review.ReviewStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class WelcomeServiceTest {

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private WelcomeService welcomeService;

    @Nested
    class 환영_문제_생성 {

        @Test
        void 신규_사용자에게_Welcome_문제와_IN_PROGRESS_리뷰를_생성한다() {
            // given
            Long userId = 1L;
            Long problemId = 100L;
            Problem welcomeProblem = Problem.from(problemId, 0, "HELLO WORLD", Platform.WELCOME);

            given(problemRepository.findProblemByproblemNumberAndPlatform(0, Platform.WELCOME))
                    .willReturn(Optional.of(welcomeProblem));
            given(reviewRepository.create(any(Review.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            // when
            welcomeService.createWelcomeProblemForUser(userId);

            // then
            ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
            verify(reviewRepository).create(reviewCaptor.capture());

            Review capturedReview = reviewCaptor.getValue();
            assertThat(capturedReview.getProblemId()).isEqualTo(problemId);
            assertThat(capturedReview.getUserId()).isEqualTo(userId);
            assertThat(capturedReview.getStatus()).isEqualTo(ReviewStatus.IN_PROGRESS);
            assertThat(capturedReview.getRevisedCode()).contains("Hello, World!");
        }

        @Test
        void Welcome_문제가_없으면_새로_생성한다() {
            // given
            Long userId = 1L;
            Long problemId = 100L;
            Problem newWelcomeProblem = Problem.from(problemId, 0, "HELLO WORLD", Platform.WELCOME);

            given(problemRepository.findProblemByproblemNumberAndPlatform(0, Platform.WELCOME))
                    .willReturn(Optional.empty());
            given(problemRepository.save(any(Problem.class)))
                    .willReturn(newWelcomeProblem);
            given(reviewRepository.create(any(Review.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            // when
            welcomeService.createWelcomeProblemForUser(userId);

            // then
            ArgumentCaptor<Problem> problemCaptor = ArgumentCaptor.forClass(Problem.class);
            verify(problemRepository).save(problemCaptor.capture());

            Problem capturedProblem = problemCaptor.getValue();
            assertThat(capturedProblem.getProblemNumber()).isEqualTo(0);
            assertThat(capturedProblem.getProblemTitle()).isEqualTo("HELLO WORLD");
            assertThat(capturedProblem.getPlatform()).isEqualTo(Platform.WELCOME);
        }
    }
}

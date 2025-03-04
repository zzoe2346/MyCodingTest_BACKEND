package com.mycodingtest.solvedproblem;

import com.mycodingtest.common.exception.InvalidOwnershipException;
import com.mycodingtest.review.Review;
import com.mycodingtest.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SolvedProblemTest {

    @Test
    @DisplayName("SolvedProblem 객체가 생성되면 기본값이 설정된다(초기 설정값이 있다)")
    void createSolvedProblem() {
        // given
        int problemNumber = 1234;
        String problemTitle = "두 수의 합";
        User user = new User("사용자", "user@example.com", "picture.jpg", "google", "oauth123");
        Review review = new Review(user);

        // when
        SolvedProblem solvedProblem = new SolvedProblem(problemNumber, problemTitle, user, review);

        // then
        assertThat(solvedProblem.getProblemNumber()).isEqualTo(problemNumber);
        assertThat(solvedProblem.getProblemTitle()).isEqualTo(problemTitle);
        assertThat(solvedProblem.getUser()).isEqualTo(user);
        assertThat(solvedProblem.getReview()).isEqualTo(review);
        assertThat(solvedProblem.isFavorited()).isFalse();
        assertThat(solvedProblem.getRecentSubmitAt()).isNull();
        assertThat(solvedProblem.getRecentResultText()).isNull();
    }

    @Test
    @DisplayName("최근 제출 결과를 업데이트한다")
    void updateRecentResult() {
        // given
        User user = new User("사용자", "user@example.com", "picture.jpg", "google", "oauth123");
        Review review = new Review(user);
        SolvedProblem solvedProblem = new SolvedProblem(1234, "두 수의 합", user, review);

        LocalDateTime submittedAt = LocalDateTime.now();
        String resultText = "맞았습니다!!";

        // when
        solvedProblem.updateRecentResult(submittedAt, resultText);

        // then
        assertThat(solvedProblem.getRecentSubmitAt()).isEqualTo(submittedAt);
        assertThat(solvedProblem.getRecentResultText()).isEqualTo(resultText);
    }

    @Test
    @DisplayName("즐겨찾기 상태를 반전시킨다")
    void reverseFavoriteStatus() {
        // given
        User user = new User("사용자", "user@example.com", "picture.jpg", "google", "oauth123");
        Review review = new Review(user);
        SolvedProblem solvedProblem = new SolvedProblem(1234, "두 수의 합", user, review);

        // when 이때 true로 변경됨!
        solvedProblem.reverseFavoriteStatus();

        // then
        assertThat(solvedProblem.isFavorited()).isTrue();
    }

    @Test
    @DisplayName("소유권 검증에 성공한다")
    void validateOwnership_Success() {
        // given
        User user = mock(User.class);
        given(user.getId()).willReturn(1L);

        Review review = new Review(user);
        SolvedProblem solvedProblem = new SolvedProblem(1234, "두 수의 합", user, review);

        // when & then
        solvedProblem.validateOwnership(1L); // 예외가 발생하지 않으면 성공
    }

    @Test
    @DisplayName("소유권 검증에 실패하면 예외가 발생한다")
    void validateOwnership_Fail() {
        // given
        User user = mock(User.class);
        given(user.getId()).willReturn(99909L);

        Review review = new Review(user);
        SolvedProblem solvedProblem = new SolvedProblem(1234, "두 수의 합", user, review);

        // when & then
        assertThrows(InvalidOwnershipException.class, () -> solvedProblem.validateOwnership(2L));
    }

    @Test
    @DisplayName("SolvedProblem에서 no Args 생성자가 가능해야 한다")
    void no_Args() {
        // when then
        assertDoesNotThrow(() -> new SolvedProblem());
    }
}
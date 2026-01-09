package com.mycodingtest.judgmentresult;

import com.mycodingtest.common.exception.NotOurUserException;
import com.mycodingtest.judgmentresult.dto.JudgmentResultResponse;
import com.mycodingtest.judgmentresult.dto.JudgmentResultSaveRequest;
import com.mycodingtest.review.Review;
import com.mycodingtest.problem.SolvedProblem;
import com.mycodingtest.problem.SolvedProblemRepository;
import com.mycodingtest.storage.StorageService;
import com.mycodingtest.user.domain.User;
import com.mycodingtest.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Tag("unit")
@MockitoSettings
class JudgmentResultServiceTest {

    @Mock
    private JudgmentResultRepository judgmentResultRepository;
    @Mock
    private SolvedProblemRepository solvedProblemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StorageService storageService;
    @InjectMocks
    private JudgmentResultService judgmentResultService;

    @Test
    @DisplayName("새로운 채점 결과를 저장한다")
    void saveJudgmentResult() {
        // given
        Long userId = 1L;
        User user = mock(User.class);
        given(user.getId()).willReturn(userId);
        JudgmentResultSaveRequest request = new JudgmentResultSaveRequest(
                1L,
                "zzoe2346",
                1023,
                "문제 제목",
                "맞았습니다!!",
                100,
                10,
                "java",
                100,
                LocalDateTime.now()
        );
        SolvedProblem solvedProblem = new SolvedProblem(
                1023,
                "문제 제목",
                user,
                new Review(user)
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(solvedProblemRepository.findByUserIdAndProblemNumber(user.getId(), request.problemNumber()))
                .willReturn(Optional.empty());
        given(solvedProblemRepository.save(any(SolvedProblem.class))).willReturn(solvedProblem);

        // when
        judgmentResultService.saveJudgmentResult(request, userId);

        // then
        verify(judgmentResultRepository).save(any(JudgmentResult.class));
        verify(solvedProblemRepository).save(any(SolvedProblem.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 채점 결과 저장 시 예외가 발생한다")
    void saveJudgmentResult_UserNotFound() {
        // given
        Long userId = 999L;
        JudgmentResultSaveRequest request = new JudgmentResultSaveRequest(
                1L,
                "zzoe2346",
                1023,
                "1024",
                "맞았습니다!!",
                100,
                10,
                "java",
                100,
                LocalDateTime.now()
        );

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when then
        assertThrows(NotOurUserException.class,
                () -> judgmentResultService.saveJudgmentResult(request, userId));
    }

    @Test
    @DisplayName("채점 결과 목록을 조회한다")
    void getJudgmentResultList() {
        // given
        Long solvedProblemId = 1L;
        Long userId = 1L;
        User user = new User("name", "email", "picture", "provider", "oauthId");
        SolvedProblem solvedProblem = new SolvedProblem(1000, "테스트 문제", user, new Review(user));
        JudgmentResult judgmentResult = new JudgmentResult(
                "test123", 100, "Java", 1024,
                1000, "맞았습니다!!", 12345L,
                LocalDateTime.now(), 100, user, solvedProblem
        );

        given(judgmentResultRepository.findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(solvedProblemId, userId))
                .willReturn(List.of(judgmentResult));

        // when
        List<JudgmentResultResponse> results = judgmentResultService.getJudgmentResultList(solvedProblemId, userId);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().baekjoonId()).isEqualTo("test123");
        assertThat(results.getFirst().language()).isEqualTo("Java");
    }

    @Test
    @DisplayName("제출 ID 중복 여부를 확인한다")
    void isSubmissionIdDuplicated() {
        // given
        Long submissionId = 12345L;
        given(judgmentResultRepository.existsBySubmissionId(submissionId)).willReturn(true);

        // when
        boolean isDuplicated = judgmentResultService.isSubmissionIdDuplicated(submissionId);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    @DisplayName("코드 읽기 URL을 조회한다")
    void getCodeReadUrl() {
        // given
        String submissionId = "12345";
        Long userId = 1L;
        String expectedUrl = "https://example.com/read";
        given(storageService.getCodeReadUrl(submissionId, userId)).willReturn(expectedUrl);

        // when
        String url = judgmentResultService.getCodeReadUrl(submissionId, userId);

        // then
        assertThat(url).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("코드 수정 URL을 조회한다")
    void getCodeUpdateUrl() {
        // given
        String submissionId = "12345";
        Long userId = 1L;
        String expectedUrl = "https://example.com/update";
        given(storageService.getCodeUpdateUrl(submissionId, userId)).willReturn(expectedUrl);

        // when
        String url = judgmentResultService.getCodeUpdateUrl(submissionId, userId);

        // then
        assertThat(url).isEqualTo(expectedUrl);
    }
}
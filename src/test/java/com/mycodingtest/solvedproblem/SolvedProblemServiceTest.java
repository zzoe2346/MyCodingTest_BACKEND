package com.mycodingtest.solvedproblem;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.judgmentresult.JudgmentResult;
import com.mycodingtest.judgmentresult.JudgmentResultRepository;
import com.mycodingtest.review.Review;
import com.mycodingtest.solvedproblem.dto.SolvedProblemWithReviewResponse;
import com.mycodingtest.solvedproblemtag.dto.MyTagListResponse;
import com.mycodingtest.storage.StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@MockitoSettings
class SolvedProblemServiceTest {

    @Mock
    private SolvedProblemRepository solvedProblemRepository;
    @Mock
    private JudgmentResultRepository judgmentResultRepository;
    @Mock
    private StorageService storageService;
    @InjectMocks
    private SolvedProblemService solvedProblemService;

    @Test
    @DisplayName("사용자가 푼 문제 + 복습(리뷰) 정보 목록을 페이지로 조회한다")
    void getSolvedProblemWithReviewPage() {
        // given
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();
        SolvedProblemWithReviewResponse response = new SolvedProblemWithReviewResponse(
                1L,
                1104,
                "문제 제목",
                LocalDateTime.now(),
                "result text",
                true,
                1L,
                5,
                5,
                false,
                LocalDateTime.now()
        );
        List<SolvedProblemWithReviewResponse> responseList = List.of(response);
        Page<SolvedProblemWithReviewResponse> expectedPage = new PageImpl<>(responseList);

        given(solvedProblemRepository.findAllByUserId(userId, pageable)).willReturn(expectedPage);

        // when
        Page<SolvedProblemWithReviewResponse> result = solvedProblemService.getSolvedProblemWithRiviewPage(pageable, userId);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().problemTitle()).isEqualTo("문제 제목");

        verify(solvedProblemRepository).findAllByUserId(userId, pageable);
    }

    @Test
    @DisplayName("문제의 즐겨찾기 상태를 변경한다")
    void changeFavorite() {
        // given
        Long solvedProblemId = 1L;
        Long userId = 1L;
        SolvedProblem solvedProblem = mock(SolvedProblem.class);

        given(solvedProblemRepository.findById(solvedProblemId)).willReturn(Optional.of(solvedProblem));

        // when
        solvedProblemService.changeFavorite(solvedProblemId, userId);

        // then
        verify(solvedProblemRepository).findById(solvedProblemId);
        verify(solvedProblem).validateOwnership(userId);
        verify(solvedProblem).reverseFavoriteStatus();
    }

    @Test
    @DisplayName("존재하지 않는 문제의 즐겨찾기 상태를 변경하려고 하면 예외가 발생한다")
    void changeFavorite_ThrowsException() {
        // given
        Long solvedProblemId = 999L;
        Long userId = 1L;

        given(solvedProblemRepository.findById(solvedProblemId)).willReturn(Optional.empty());

        // when then
        assertThrows(ResourceNotFoundException.class, () -> solvedProblemService.changeFavorite(solvedProblemId, userId));
    }

    @Test
    @DisplayName("리뷰 상태에 따른 푼 문제 목록을 조회한다")
    void getSolvedProblemsByReviewedStatus() {
        // given
        Long userId = 1L;
        boolean isReviewed = true;
        Pageable pageable = Pageable.unpaged();
        SolvedProblemWithReviewResponse response = mock(SolvedProblemWithReviewResponse.class);
        given(response.isReviewed()).willReturn(true);
        List<SolvedProblemWithReviewResponse> responseList = List.of(response);
        Page<SolvedProblemWithReviewResponse> expectedPage = new PageImpl<>(responseList);

        given(solvedProblemRepository.findAllByUserIdAndReviewed(userId, isReviewed, pageable)).willReturn(expectedPage);

        // when
        Page<SolvedProblemWithReviewResponse> result = solvedProblemService.getSolvedProblemsByReviewedStatus(isReviewed, pageable, userId);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().isReviewed()).isTrue();
        verify(solvedProblemRepository).findAllByUserIdAndReviewed(userId, isReviewed, pageable);
    }

    @Test
    @DisplayName("즐겨찾기한 푼 문제 목록을 조회한다")
    void getFavoriteSolvedProblem() {
        // given
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();
        SolvedProblemWithReviewResponse response = mock(SolvedProblemWithReviewResponse.class);
        given(response.isFavorite()).willReturn(true);

        List<SolvedProblemWithReviewResponse> responseList = List.of(response);
        Page<SolvedProblemWithReviewResponse> expectedPage = new PageImpl<>(responseList);

        given(solvedProblemRepository.findAllByUserIdAndFavoriteIsTrue(userId, pageable)).willReturn(expectedPage);

        // when
        Page<SolvedProblemWithReviewResponse> result = solvedProblemService.getFavoriteSolvedProblem(pageable, userId);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().isFavorite()).isTrue();
        verify(solvedProblemRepository).findAllByUserIdAndFavoriteIsTrue(userId, pageable);
    }

    @Test
    @DisplayName("특정 태그가 지정된 푼 문제 목록을 조회한다")
    void getTaggedSolvedProblem() {
        // given
        Long tagId = 5L;
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();
        SolvedProblemWithReviewResponse response = mock(SolvedProblemWithReviewResponse.class);

        List<SolvedProblemWithReviewResponse> responseList = List.of(response);
        Page<SolvedProblemWithReviewResponse> expectedPage = new PageImpl<>(responseList);

        given(solvedProblemRepository.findByAlgorithmTagAndUserId(tagId, userId, pageable)).willReturn(expectedPage);

        // when
        Page<SolvedProblemWithReviewResponse> result = solvedProblemService.getTaggedSolvedProblem(tagId, pageable, userId);

        // then
        assertThat(result.getContent()).hasSize(1);
        verify(solvedProblemRepository).findByAlgorithmTagAndUserId(tagId, userId, pageable);
    }

    @Test
    @DisplayName("내가 사용한 태그 목록을 조회한다")
    void getMyTagList() {
        // given
        Long userId = 1L;
        List<Long> tagIds = List.of(1L, 2L, 3L);

        given(solvedProblemRepository.findDistinctTagIdsByUserId(userId)).willReturn(tagIds);

        // when
        MyTagListResponse result = solvedProblemService.getMyTagList(userId);

        // then
        assertThat(result.tagIds()).hasSize(3);
        assertThat(result.tagIds()[0]).isEqualTo(1L);
        assertThat(result.tagIds()[1]).isEqualTo(2L);
        assertThat(result.tagIds()[2]).isEqualTo(3L);
        verify(solvedProblemRepository).findDistinctTagIdsByUserId(userId);
    }

    @Test
    @DisplayName("푼 문제를 삭제한다")
    void deleteSolvedProblem() {
        // given
        Long solvedProblemId = 1L;
        Long userId = 1L;
        Long reviewId = 10L;

        SolvedProblem solvedProblem = mock(SolvedProblem.class);
        Review review = mock(Review.class);
        given(solvedProblem.getReview()).willReturn(review);
        given(review.getId()).willReturn(reviewId);

        JudgmentResult judgmentResult1 = mock(JudgmentResult.class);
        JudgmentResult judgmentResult2 = mock(JudgmentResult.class);
        given(judgmentResult1.getSubmissionId()).willReturn(100L);
        given(judgmentResult2.getSubmissionId()).willReturn(200L);
        List<JudgmentResult> judgmentResults = List.of(judgmentResult1, judgmentResult2);

        given(solvedProblemRepository.findById(solvedProblemId)).willReturn(Optional.of(solvedProblem));
        given(judgmentResultRepository.findAllBySolvedProblem(solvedProblem)).willReturn(judgmentResults);

        // when
        solvedProblemService.deleteSolvedProblem(solvedProblemId, userId);

        // then
        verify(solvedProblem).validateOwnership(userId);
        verify(storageService).deleteMemo(String.valueOf(reviewId), userId);
        verify(storageService).deleteCodes(anyList(), eq(userId));
        verify(solvedProblemRepository).delete(solvedProblem);
        verify(judgmentResultRepository).deleteAll(judgmentResults);
    }
}

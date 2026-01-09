package com.mycodingtest.solvedproblemtag;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.problem.SolvedProblem;
import com.mycodingtest.problem.SolvedProblemRepository;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagResponse;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagSetRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Tag("unit")
@MockitoSettings
class SolvedProblemTagServiceTest {

    @Mock
    private SolvedProblemRepository solvedProblemRepository;
    @Mock
    private SolvedProblemTagRepository solvedProblemTagRepository;
    @InjectMocks
    private SolvedProblemTagService solvedProblemTagService;

    @Captor
    private ArgumentCaptor<Set<SolvedProblemTag>> tagListCaptor;

    @Test
    @DisplayName("알고리즘 태그를 조회한다")
    void getAlgorithmTags() {
        // given
        Long solvedProblemId = 1L;
        List<Integer> tagIds = Arrays.asList(1, 3, 5);

        given(solvedProblemRepository.findTagIdsBySolvedProblemId(solvedProblemId))
                .willReturn(tagIds);

        // when
        AlgorithmTagResponse response = solvedProblemTagService.getAlgorithmTags(solvedProblemId);

        // then
        assertThat(response.tagIds()).hasSize(3);
        assertThat(response.tagIds()[0]).isEqualTo(1);
        assertThat(response.tagIds()[1]).isEqualTo(3);
        assertThat(response.tagIds()[2]).isEqualTo(5);
    }

    @Test
    @DisplayName("알고리즘 태그를 새로 설정한다(기존의 태그 없는 경우)")
    void setAlgorithmTags() {
        // given
        Long solvedProblemId = 1L;
        Long userId = 1L;
        AlgorithmTagSetRequest request = new AlgorithmTagSetRequest(new int[]{1, 3, 5});

        SolvedProblem solvedProblem = mock(SolvedProblem.class);
        given(solvedProblemRepository.findById(solvedProblemId))
                .willReturn(Optional.of(solvedProblem));

        // 기존 태그들 조회한다. 여기선 기존 태그 없음.
        List<SolvedProblemTag> existingTags = Collections.emptyList();
        given(solvedProblemTagRepository.findAllBySolvedProblem(solvedProblem))
                .willReturn(existingTags);

        // when
        solvedProblemTagService.setAlgorithmTags(solvedProblemId, request, userId);

        // then
        verify(solvedProblemTagRepository).saveAll(tagListCaptor.capture()); //저장되는거 캡처

        Set<SolvedProblemTag> savedTags = tagListCaptor.getValue();
        assertThat(savedTags).hasSize(3);
        assertThat(savedTags.stream().map(SolvedProblemTag::getTagId))
                .containsExactlyInAnyOrder(1, 3, 5);
    }

    @Test
    @DisplayName("기존 태그를 삭제하고 새로운 태그를 추가한다")
    void updateExistingTags() {
        // given
        Long solvedProblemId = 1L;
        Long userId = 1L;
        int[] newTagIds = {2, 3, 5}; // 2, 3, 5로 변경
        AlgorithmTagSetRequest request = new AlgorithmTagSetRequest(newTagIds);

        SolvedProblem solvedProblem = mock(SolvedProblem.class);
        given(solvedProblemRepository.findById(solvedProblemId))
                .willReturn(Optional.of(solvedProblem));

        // 기존 태그: 1, 3, 4
        SolvedProblemTag tag1 = new SolvedProblemTag(solvedProblem, 1);
        SolvedProblemTag tag3 = new SolvedProblemTag(solvedProblem, 3);
        SolvedProblemTag tag4 = new SolvedProblemTag(solvedProblem, 4);
        List<SolvedProblemTag> existingTags = Arrays.asList(tag1, tag3, tag4);

        given(solvedProblemTagRepository.findAllBySolvedProblem(solvedProblem))
                .willReturn(existingTags);

        // when
        solvedProblemTagService.setAlgorithmTags(solvedProblemId, request, userId);

        // then
        // 제거할 태그: 1, 4
        ArgumentCaptor<List<SolvedProblemTag>> removeCaptor = ArgumentCaptor.forClass(List.class);
        verify(solvedProblemTagRepository).deleteAllInBatch(removeCaptor.capture());
        List<SolvedProblemTag> removedTags = removeCaptor.getValue();
        assertThat(removedTags).hasSize(2);
        assertThat(removedTags.stream().map(SolvedProblemTag::getTagId))
                .containsExactlyInAnyOrder(1, 4);

        // 저장할 태그: 2, 3, 5 (3은 유지, 2, 5는 새로 추가)
        verify(solvedProblemTagRepository).saveAll(tagListCaptor.capture());
        Set<SolvedProblemTag> savedTags = tagListCaptor.getValue();
        assertThat(savedTags).hasSize(3);
        assertThat(savedTags.stream().map(SolvedProblemTag::getTagId))
                .containsExactlyInAnyOrder(2, 3, 5);
    }

    @Test
    @DisplayName("태그를 모두 삭제한다")
    void removeAllTags() {
        // given
        Long solvedProblemId = 1L;
        Long userId = 1L;
        int[] emptyTagIds = {}; // 빈 태그 배열... 그냥 빈걸로 둔다는 뜻
        AlgorithmTagSetRequest request = new AlgorithmTagSetRequest(emptyTagIds);

        SolvedProblem solvedProblem = mock(SolvedProblem.class);
        given(solvedProblemRepository.findById(solvedProblemId))
                .willReturn(Optional.of(solvedProblem));
        doNothing().when(solvedProblem).validateOwnership(userId);

        // when
        solvedProblemTagService.setAlgorithmTags(solvedProblemId, request, userId);

        // then
        verify(solvedProblemTagRepository).deleteAllBySolvedProblem(solvedProblem);
        verify(solvedProblemTagRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("존재하지 않는 문제의 태그를 설정하면 예외가 발생한다")
    void setTagsForNonExistingProblem() {
        // given
        Long nonExistingProblemId = 999L;
        Long userId = 1L;
        int[] tagIds = {1, 2, 3};
        AlgorithmTagSetRequest request = new AlgorithmTagSetRequest(tagIds);

        given(solvedProblemRepository.findById(nonExistingProblemId))
                .willReturn(Optional.empty());

        // when then
        assertThrows(ResourceNotFoundException.class, () ->
                solvedProblemTagService.setAlgorithmTags(nonExistingProblemId, request, userId));
    }
}

package com.mycodingtest.service;

import com.mycodingtest.judgmentresult.JudgmentResultSaveRequest;
import com.mycodingtest.common.exception.NotOurUserException;
import com.mycodingtest.judgmentresult.JudgmentResultRepository;
import com.mycodingtest.judgmentresult.JudgmentResultService;
import com.mycodingtest.solvedproblem.SolvedProblemRepository;
import com.mycodingtest.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class JudgmentResultServiceTest {

    @Mock
    JudgmentResultRepository judgmentResultRepository;
    @Mock
    SolvedProblemRepository solvedProblemRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    JudgmentResultService judgmentResultService;

//    @Test
//    void test_saveJudgmentResult_when_first() {
//        //given
//        Long userId = 1L;
//        User mockUser = mock(User.class);
//        JudgmentResultSaveRequest request = new JudgmentResultSaveRequest(1L, "123", 123, "title", "123", 123, 123, "java", 100, LocalDateTime.now());
//        SolvedProblem mockSolvedProblem = new SolvedProblem(123, "title", mockUser, mock(Review.class));
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
//        when(solvedProblemRepository.findByUserIdAndProblemNumber(userId, request.problemNumber())).thenReturn(Optional.of(mockSolvedProblem));
//
//        //when
//        judgmentResultService.saveJudgmentResult(request, userId);
//
//        //then
//        verify(solvedProblemRepository, times(1)).save(any(SolvedProblem.class));
//        verify(judgmentResultRepository, times(1)).save(any(JudgmentResult.class));
//    }
//
//    @Test
//    void test_saveJudgmentResult_when_not_first() {
//        //given
//        Long userId = 1L;
//        User mockUser = mock(User.class);
//        JudgmentResultSaveRequest request = new JudgmentResultSaveRequest(1L, "123", 123, "title", "123", 123, 123, "java", 100, LocalDateTime.now());
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
//        when(solvedProblemRepository.findByUserIdAndProblemNumber(userId, request.problemNumber())).thenReturn(Optional.empty());
//
//        //when
//        judgmentResultService.saveJudgmentResult(request, userId);
//
//        //then
//        verify(solvedProblemRepository, times(1)).save(any(SolvedProblem.class));
//        verify(judgmentResultRepository, times(1)).save(any(JudgmentResult.class));
//
//        ArgumentCaptor<SolvedProblem> solvedProblemCaptor = ArgumentCaptor.forClass(SolvedProblem.class);
//        verify(solvedProblemRepository).save(solvedProblemCaptor.capture());
//
//        SolvedProblem savedSolvedProblem = solvedProblemCaptor.getValue();
//        assertNotNull(savedSolvedProblem);
//        assertEquals(request.problemNumber(), savedSolvedProblem.getProblemNumber());
//    }

    @Test
    void testSaveJudgmentResultFailWhenNotOurUser() {
        //given
        Long userId = 1L;
        JudgmentResultSaveRequest request = mock(JudgmentResultSaveRequest.class);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        assertThrows(NotOurUserException.class, () -> judgmentResultService.saveJudgmentResult(request, userId));
    }

    @Test
    void testGetJudgmentResultListSuccess() {

    }

//    @Test
//    void testGetJudgmentResultListFailWhenResultUserIdNotMatchWithUserId() {
//        //given
//        Long solvedProblemId = 1L;
//        Long userId = 1L;
//        User mockUser = mock(User.class);
//        JudgmentResult judgmentResult = mock(JudgmentResult.class);
//
//        when(judgmentResultRepository.findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(solvedProblemId, userId)).thenReturn(List.of(judgmentResult));
//        when(judgmentResult.getUser()).thenReturn(mockUser);
//        when(mockUser.getId()).thenReturn(2L);
//
//        //when then
//        assertThrows(InvalidOwnershipException.class, () -> judgmentResultService.getJudgmentResultList(solvedProblemId, userId));
//    }
}

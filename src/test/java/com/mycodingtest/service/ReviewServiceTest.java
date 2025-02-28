package com.mycodingtest.service;

import com.mycodingtest.review.dto.ReviewRatingLevelsUpdateRequest;
import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.review.ReviewRepository;
import com.mycodingtest.review.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.mockito.Mockito.*;

@MockitoSettings
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

//    @Test
//    void testUpdateReviewRatingLevelsSuccess() {
//        //given
//        ReviewRatingLevelsUpdateRequest request = mock(ReviewRatingLevelsUpdateRequest.class);
//        Long reviewId = 1L;
//        Review review = mock(Review.class);
//        User user = mock(User.class);
//        Long userId = 1L;
//
//        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
//        when(review.getUser()).thenReturn(user);
//        when(user.getId()).thenReturn(1L);
//        when(request.difficultyLevel()).thenReturn(1);
//        when(request.importanceLevel()).thenReturn(2);
//
//        //when
//        reviewService.updateReviewRatingLevels(request, reviewId, userId);
//
//        //then
//        verify(review, times(1)).updateRatingLevels(request.difficultyLevel(), request.importanceLevel());
//    }

    @Test
    void testUpdateReviewRatingLevelsFailWhenResourceNotFound() {
        //given
        ReviewRatingLevelsUpdateRequest request = mock(ReviewRatingLevelsUpdateRequest.class);
        Long reviewId = 1L;
        Long userId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        //when then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReviewRatingLevels(request, reviewId, userId));
    }
}

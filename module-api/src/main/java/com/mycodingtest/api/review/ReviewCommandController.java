package com.mycodingtest.api.review;

import com.mycodingtest.api.review.dto.request.UpdateReviewRequest;
import com.mycodingtest.api.review.dto.response.ReviewRecentStatusResponse;
import com.mycodingtest.application.review.ReviewService;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "복습", description = "복습 관련 API")
public class ReviewCommandController {

    private final ReviewService reviewService;

    @PutMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 정보 통합 수정", description = "리뷰의 모든 필드를 선택적으로 수정합니다.")
    public ResponseEntity<ReviewRecentStatusResponse> updateReview(@PathVariable Long reviewId,
                                                                   @RequestBody UpdateReviewRequest request,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        Review updatedReview = reviewService.updateReview(request.toCommand(reviewId, userDetails.getUserId()));
        return ResponseEntity.ok(ReviewRecentStatusResponse.from(updatedReview));
    }

}

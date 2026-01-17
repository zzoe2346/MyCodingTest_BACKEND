package com.mycodingtest.api.review;

import com.mycodingtest.api.review.dto.response.ReviewCountStatusInToDoResponse;
import com.mycodingtest.api.review.dto.response.ReviewResponse;
import com.mycodingtest.application.review.ReviewService;
import com.mycodingtest.application.review.dto.PagedResult;
import com.mycodingtest.application.review.dto.ReviewSummary;
import com.mycodingtest.domain.review.ReviewStatus;
import com.mycodingtest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "복습", description = "복습 관련 API")
public class ReviewQueryController {

    private final ReviewService reviewService;

    @GetMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 유무, 날짜, 체감 난이도, 재복습 필요도 획득")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                ReviewResponse.from(reviewService.getReview(reviewId, userDetails.getUserId())));
    }

    @GetMapping("/api/reviews/unreviewed/count")
    @Operation(summary = "리뷰를 기다리는 문제 개수 반환")
    public ResponseEntity<ReviewCountStatusInToDoResponse> getWaitReviewCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                ReviewCountStatusInToDoResponse
                        .from(reviewService.getReviewCountStatusInToDo(userDetails.getUserId())));
    }

    @GetMapping("/api/reviews")
    public ResponseEntity<PagedResult<ReviewSummary>> getReviewSummaries(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(required = false, defaultValue = "TO_DO") ReviewStatus filter) {
        return ResponseEntity.ok(
                reviewService.getReviewSummaries(userDetails.getUserId(), page, size, filter));
    }

}

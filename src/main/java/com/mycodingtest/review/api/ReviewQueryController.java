package com.mycodingtest.review.api;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.review.application.ReviewService;
import com.mycodingtest.review.api.dto.ReviewResponse;
import com.mycodingtest.review.api.dto.WaitReviewCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "복습", description = "복습 관련 API")
public class ReviewQueryController {

    private final ReviewService reviewService;

    @GetMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 유무, 날짜, 체감 난이도, 재복습 필요도 획득")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reviewService.getReview(reviewId, userDetails.getUserId()));
    }

    @GetMapping("/api/reviews/unreviewed-counting")
    @Operation(summary = "리뷰를 기다리는 문제 개수 반환")
    public ResponseEntity<WaitReviewCountResponse> getWaitReviewCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reviewService.getWaitReviewCount(userDetails.getUserId()));
    }
}

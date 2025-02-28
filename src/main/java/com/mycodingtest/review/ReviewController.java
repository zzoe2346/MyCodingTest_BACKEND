package com.mycodingtest.review;

import com.mycodingtest.review.dto.ReviewRatingLevelsUpdateRequest;
import com.mycodingtest.review.dto.ReviewRecentStatusResponse;
import com.mycodingtest.review.dto.ReviewResponse;
import com.mycodingtest.review.dto.WaitReviewCountResponse;
import com.mycodingtest.security.CustomUserDetails;
import com.mycodingtest.storage.dto.UrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "복습", description = "복습 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PutMapping("/api/solved-problems/reviews/{reviewId}/levels")
    @Operation(summary = "체감 난이도, 재복습 필요도 수정")
    public ResponseEntity<Void> updateReviewRatingLevels(@RequestBody ReviewRatingLevelsUpdateRequest request,
                                                         @PathVariable Long reviewId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.updateReviewRatingLevels(request, reviewId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/solved-problems/reviews/{reviewId}")
    @Operation(summary = "리뷰 유무, 날짜, 체감 난이도, 재복습 필요도 획득")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reviewService.getReview(reviewId, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/reviews/{reviewId}/memo/update")
    @Operation(summary = "메모 수정/저장 URL 획득")
    public ResponseEntity<UrlResponse> saveMemo(@PathVariable Long reviewId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reviewService.getMemoUpdateUrl(reviewId, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/reviews/{reviewId}/memo/read")
    @Operation(summary = "메모 읽기 URL 획득")
    public ResponseEntity<UrlResponse> getMemo(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reviewService.getMemoReadUrl(reviewId, userDetails.getUserId()));
    }

    @PutMapping("/api/solved-problems/reviews/{reviewId}/status")
    @Operation(summary = "리뷰 상태를 완료로 전환 시킴. 최신 상태 응답.")
    public ResponseEntity<ReviewRecentStatusResponse> updateReviewStatus(@PathVariable Long reviewId,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(reviewService.updateReviewStatus(reviewId, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/unreviewed-count")
    @Operation(summary = "리뷰를 기다리는 문제 개수 반환")
    public ResponseEntity<WaitReviewCountResponse> getWaitReviewCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reviewService.getWaitReviewCount(userDetails.getUserId()));
    }
}

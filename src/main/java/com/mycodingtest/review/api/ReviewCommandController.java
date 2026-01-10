package com.mycodingtest.review.api;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.review.application.ReviewService;
import com.mycodingtest.review.dto.ReviewRatingLevelsUpdateRequest;
import com.mycodingtest.review.dto.ReviewRecentStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "복습", description = "복습 관련 API")
public class ReviewCommandController {

    private final ReviewService reviewService;

    public ReviewCommandController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //TODO: 리뷰로 이동
//    @PatchMapping("/api/solved-problems/{solvedProblemId}/favorite")
//    public ResponseEntity<Void> changeFavorite(@PathVariable Long solvedProblemId,
//                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
//        problemService.changeFavorite(solvedProblemId, userDetails.getUserId());
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/api/solved-problems/reviews/{reviewId}/levels")
    @Operation(summary = "체감 난이도, 재복습 필요도 수정")
    public ResponseEntity<Void> updateReviewRatingLevels(@RequestBody ReviewRatingLevelsUpdateRequest request,
                                                         @PathVariable Long reviewId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.updateReviewRatingLevels(request, reviewId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

//
//    @GetMapping("/api/solved-problems/reviews/{reviewId}/memo/update")
//    @Operation(summary = "메모 수정/저장 URL 획득")
//    public ResponseEntity<UrlResponse> saveMemo(@PathVariable Long reviewId,
//                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
//        return ResponseEntity.ok(reviewService.getMemoUpdateUrl(reviewId, userDetails.getUserId()));
//    }

    @PutMapping("/api/solved-problems/reviews/{reviewId}/status")
    @Operation(summary = "리뷰 상태를 완료로 전환 시킴. 최신 상태 응답.")
    public ResponseEntity<ReviewRecentStatusResponse> updateReviewStatus(@PathVariable Long reviewId,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(reviewService.updateReviewStatus(reviewId, userDetails.getUserId()));
    }

}

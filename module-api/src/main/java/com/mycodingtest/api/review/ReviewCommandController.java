package com.mycodingtest.api.review;

import com.mycodingtest.application.review.ReviewService;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.api.review.dto.ReviewRatingLevelsUpdateRequest;
import com.mycodingtest.api.review.dto.ReviewRecentStatusResponse;
import com.mycodingtest.application.review.dto.UpdateReviewRatingLevelCommand;
import com.mycodingtest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "복습", description = "복습 관련 API")
public class ReviewCommandController {

    private final ReviewService reviewService;

    @PutMapping("/api/reviews/{reviewId}/favorite")
    @Operation(summary = "즐겨찾기 변경", description = "리뷰의 즐겨찾기 상태를 변경합니다.")
    public ResponseEntity<Void> changeFavorite(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.changeFavorite(reviewId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/reviews/{reviewId}/levels")
    @Operation(summary = "체감 난이도, 재복습 필요도 수정")
    public ResponseEntity<Void> updateReviewRatingLevels(@RequestBody ReviewRatingLevelsUpdateRequest request,
                                                         @PathVariable Long reviewId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        //TODO 수정 필수
        reviewService.updateReviewRatingLevels(new UpdateReviewRatingLevelCommand(request.difficultyLevel(), request.importanceLevel()), reviewId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/reviews/{reviewId}/status")
    @Operation(summary = "리뷰 상태를 완료로 전환 시킴. 최신 상태 응답.")
    public ResponseEntity<ReviewRecentStatusResponse> updateReviewStatus(@PathVariable Long reviewId,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        //TODO 지금은 그냥 완료로 바뀌도록 한다 추후 고도화 대상
        Review review = reviewService.updateReviewStatus(reviewId, userDetails.getUserId());
        return ResponseEntity.ok(new ReviewRecentStatusResponse(review.getStatus(), review.getReviewedAt()));
    }

    @PutMapping("/api/reviews/{reviewId}/code")
    @Operation(summary = "코드 수정")
    public ResponseEntity<Void> updateCode(@PathVariable Long reviewId,
                                           @RequestBody String code,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.updateReviewCode(reviewId, userDetails.getUserId(), code);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/reviews/{reviewId}/content")
    @Operation(summary = "리뷰 내용(메모) 수정")
    public ResponseEntity<Void> updateMemo(@PathVariable Long reviewId,
                                           @RequestBody String content,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.updateReviewMemo(reviewId, userDetails.getUserId(), content);
        return ResponseEntity.ok().build();
    }

}

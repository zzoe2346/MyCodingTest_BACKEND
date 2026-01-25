package com.mycodingtest.api.review;

import com.mycodingtest.api.review.dto.request.UpdateReviewRequest;
import com.mycodingtest.api.review.dto.response.ReviewRecentStatusResponse;
import com.mycodingtest.application.review.command.ReviewCommandService;
import com.mycodingtest.application.review.command.UpdateReviewCommand;
import com.mycodingtest.application.review.command.UpdateReviewResult;
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

    private final ReviewCommandService reviewCommandService;

    @PutMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 정보 통합 수정", description = "리뷰의 모든 필드를 선택적으로 수정합니다.")
    public ResponseEntity<UpdatedReviewResponse> updateReview(@PathVariable Long reviewId,
                                                              @RequestBody UpdateReviewRequest request,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        UpdateReviewResult updatedReview = reviewCommandService.updateReview(
                UpdateReviewCommand.from(
                        reviewId,
                        userDetails.getUserId(),
                        request.isFavorite(),
                        request.difficultyLevel(),
                        request.importanceLevel(),
                        request.code(),
                        request.content(),
                        request.status()));
        return ResponseEntity.ok(UpdatedReviewResponse.from(updatedReview));
    }

}

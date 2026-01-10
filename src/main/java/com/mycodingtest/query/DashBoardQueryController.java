package com.mycodingtest.query;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.query.dto.ReviewSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "대시보드", description = "대시보드 관련 API")
public class DashBoardQueryController {

    private final DashBoardQueryRepository dashBoardQueryRepository;

    @GetMapping("/api/solved-problems/all")
    @Operation(summary = "푼 문제 목록 조회", description = "푼 문제 목록을 조회합니다.")
    public ResponseEntity<Page<ReviewSummaryResponse>> getSolvedProblemWithReviewPage(@PageableDefault Pageable pageable,
                                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(dashBoardQueryRepository.findAllProblemWithReviewByUserId(userDetails.getUserId(), pageable));
    }

    @GetMapping("/api/solved-problems")
    @Operation(summary = "리뷰 상태별 푼 문제 목록 조회", description = "리뷰 상태별 푼 문제 목록을 조회합니다.")
    public ResponseEntity<Page<ReviewSummaryResponse>> getSolvedProblemByReviewStatus(@RequestParam boolean isReviewed,
                                                                                      @PageableDefault Pageable pageable,
                                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(dashBoardQueryRepository.findAllByUserIdAndReviewed(userDetails.getUserId(), isReviewed, pageable));
    }

    @GetMapping("/api/solved-problems/favorite")
    @Operation(summary = "즐겨찾기한 문제 목록 조회", description = "즐겨찾기한 문제 목록을 조회합니다.")
    public ResponseEntity<Page<ReviewSummaryResponse>> getFavoriteSolvedProblem(@RequestParam boolean isFavorite,
                                                                                @PageableDefault Pageable pageable,
                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(dashBoardQueryRepository.findAllByUserIdAndFavoriteIsTrue(userDetails.getUserId(), pageable));
    }

    // TAG 보류
//    @GetMapping("/api/solved-problems/tags/{tagId}")
//    public ResponseEntity<Page<ReviewSummaryResponse>> getTaggedSolvedProblem(@PathVariable Long tagId,
//                                                                              @PageableDefault Pageable pageable,
//                                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
//        return ResponseEntity.ok(problemService.getTaggedSolvedProblem(tagId, pageable, userDetails.getUserId()));
//    }
}

package com.mycodingtest.query;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.query.dto.ReviewSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashBoardQueryController {

    private final DashBoardQueryRepository dashBoardQueryRepository;

    @GetMapping("/api/solved-problems")
    public ResponseEntity<Page<ReviewSummaryResponse>> getSolvedProblemWithReviewPage(@PageableDefault Pageable pageable,
                                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(dashBoardQueryRepository.findAllProblemWithReviewByUserId(userDetails.getUserId(), pageable));
    }

    @GetMapping("/api/solved-problems/review/{isReviewed}")
    public ResponseEntity<Page<ReviewSummaryResponse>> getSolvedProblemByReviewStatus(@PathVariable boolean isReviewed,
                                                                                      @PageableDefault Pageable pageable,
                                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(dashBoardQueryRepository.findAllByUserIdAndReviewed(userDetails.getUserId(), isReviewed, pageable));
    }

    @GetMapping("/api/solved-problems/favorites")
    public ResponseEntity<Page<ReviewSummaryResponse>> getFavoriteSolvedProblem(@PageableDefault Pageable pageable,
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

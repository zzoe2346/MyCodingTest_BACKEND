package com.mycodingtest.problem.api;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.problem.application.ProblemService;
import com.mycodingtest.problem.dto.ProblemWithReviewResponse;
import com.mycodingtest.solvedproblemtag.dto.MyTagListResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "푼 문제", description = "푼 문제 정보 관련 API")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/api/solved-problems")
    public ResponseEntity<Page<ProblemWithReviewResponse>> getSolvedProblemWithReviewPage(@PageableDefault Pageable pageable,
                                                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(problemService.getSolvedProblemWithRiviewPage(pageable, userDetails.getUserId()));
    }

    @PatchMapping("/api/solved-problems/{solvedProblemId}/favorite")
    public ResponseEntity<Void> changeFavorite(@PathVariable Long solvedProblemId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        problemService.changeFavorite(solvedProblemId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/solved-problems/review/{isReviewed}")
    public ResponseEntity<Page<ProblemWithReviewResponse>> getSolvedProblemByReviewStatus(@PathVariable boolean isReviewed,
                                                                                          @PageableDefault Pageable pageable,
                                                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(problemService.getSolvedProblemsByReviewedStatus(isReviewed, pageable, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/favorites")
    public ResponseEntity<Page<ProblemWithReviewResponse>> getFavoriteSolvedProblem(@PageableDefault Pageable pageable,
                                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(problemService.getFavoriteSolvedProblem(pageable, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/tags/{tagId}")
    public ResponseEntity<Page<ProblemWithReviewResponse>> getTaggedSolvedProblem(@PathVariable Long tagId,
                                                                                  @PageableDefault Pageable pageable,
                                                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(problemService.getTaggedSolvedProblem(tagId, pageable, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/tags")
    public ResponseEntity<MyTagListResponse> getMyTagList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(problemService.getMyTagList(userDetails.getUserId()));
    }

    @DeleteMapping("/api/solved-problems/{solvedProblemId}")
    public ResponseEntity<Void> deleteSolvedProblem(@PathVariable Long solvedProblemId,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        problemService.deleteSolvedProblem(solvedProblemId, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

}

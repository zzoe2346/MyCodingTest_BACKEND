package com.mycodingtest.solvedproblem;

import com.mycodingtest.security.CustomUserDetails;
import com.mycodingtest.solvedproblem.dto.SolvedProblemWithReviewResponse;
import com.mycodingtest.tag.MyTagListResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "푼 문제", description = "푼 문제 정보 관련 API")
public class SolvedProblemController {

    private final SolvedProblemService solvedProblemService;

    public SolvedProblemController(SolvedProblemService solvedProblemService) {
        this.solvedProblemService = solvedProblemService;
    }

    @GetMapping("/api/solved-problems")
    public ResponseEntity<Page<SolvedProblemWithReviewResponse>> getSolvedProblemWithReviewPage(@PageableDefault Pageable pageable,
                                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(solvedProblemService.getSolvedProblemWithRiviewPage(pageable, userDetails.getUserId()));
    }

    @PatchMapping("/api/solved-problems/{solvedProblemId}/favorite")
    public ResponseEntity<Void> changeFavorite(@PathVariable Long solvedProblemId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        solvedProblemService.changeFavorite(solvedProblemId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/solved-problems/review/{isReviewed}")
    public ResponseEntity<Page<SolvedProblemWithReviewResponse>> getSolvedProblemByReviewStatus(@PathVariable boolean isReviewed,
                                                                                                @PageableDefault Pageable pageable,
                                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(solvedProblemService.getSolvedProblemsByReviewedStatus(isReviewed, pageable, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/favorites")
    public ResponseEntity<Page<SolvedProblemWithReviewResponse>> getFavoriteSolvedProblem(@PageableDefault Pageable pageable,
                                                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(solvedProblemService.getFavoriteSolvedProblem(pageable, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/tags/{tagId}")
    public ResponseEntity<Page<SolvedProblemWithReviewResponse>> getTaggedSolvedProblem(@PathVariable Long tagId,
                                                                                        @PageableDefault Pageable pageable,
                                                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(solvedProblemService.getTaggedSolvedProblem(tagId, pageable, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/tags")
    public ResponseEntity<MyTagListResponse> getMyTagList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(solvedProblemService.getMyTagList(userDetails.getUserId()));
    }

    @DeleteMapping("/api/solved-problems/{solvedProblemId}")
    public ResponseEntity<Void> deleteSolvedProblem(@PathVariable Long solvedProblemId,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        solvedProblemService.deleteSolvedProblem(solvedProblemId, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }
}

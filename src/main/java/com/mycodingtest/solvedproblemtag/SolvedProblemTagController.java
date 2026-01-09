package com.mycodingtest.solvedproblemtag;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagResponse;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagSetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "알고리즘 태그", description = "알고리즘 태그 관련 API")
public class SolvedProblemTagController {

    private final SolvedProblemTagService solvedProblemTagService;

    public SolvedProblemTagController(SolvedProblemTagService solvedProblemTagService) {
        this.solvedProblemTagService = solvedProblemTagService;
    }

    @PutMapping("/api/solved-problems/{solvedProblemId}/tags")
    @Operation(summary = "태그 재설정(최대 7개)", description = "7개 까지만 태그 걸수 있음. 7 초과시 Bad Request")
    public ResponseEntity<Void> setTags(@PathVariable Long solvedProblemId,
                                        @RequestBody @Valid AlgorithmTagSetRequest request,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        solvedProblemTagService.setAlgorithmTags(solvedProblemId, request, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/solved-problems/{solvedProblemId}/tags")
    @Operation(summary = "문제의 태그 조회", description = "문제에 걸린 태그 조회")
    public ResponseEntity<AlgorithmTagResponse> getTags(@PathVariable Long solvedProblemId) {
        return ResponseEntity.ok(solvedProblemTagService.getAlgorithmTags(solvedProblemId));
    }
}

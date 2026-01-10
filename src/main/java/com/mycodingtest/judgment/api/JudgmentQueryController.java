package com.mycodingtest.judgment.api;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.judgment.application.JudgmentService;
import com.mycodingtest.judgment.dto.JudgmentResultResponse;
import com.mycodingtest.storage.dto.UrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "채점 결과", description = "채점 결과 관련 API")
@RequiredArgsConstructor
public class JudgmentQueryController {

    private final JudgmentService judgmentService;

    @GetMapping("/api/solved-problems/{solvedProblemId}/judgment-results")
    @Operation(summary = "채점 결과 목록 조회", description = "특정 문제의 채점 결과 목록을 조회합니다.")
    public ResponseEntity<List<JudgmentResultResponse>> getJudgmentResultList(@PathVariable Long solvedProblemId,
                                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(judgmentService.getJudgmentResultList(solvedProblemId, userDetails.getUserId()));
    }

    @Deprecated
    @GetMapping("/api/solved-problems/judgment-results/submission-code/read/{submissionId}")
    @Operation(summary = "제출 소스 코드 읽기 URL 획득")
    public ResponseEntity<UrlResponse> getCodeReadUrl(@PathVariable String submissionId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        String url = judgmentService.getCodeReadUrl(submissionId, userDetails.getUserId());
        return ResponseEntity.ok(new UrlResponse(url));
    }

}

package com.mycodingtest.judgment.api;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.judgment.application.JudgmentService;
import com.mycodingtest.storage.dto.UrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "채점 결과", description = "채점 결과 관련 API")
@RequiredArgsConstructor
public class JudgmentCommandController {

    private final JudgmentService judgmentService;

    @Deprecated//TOOD 수정, 제거 로 분할
    @GetMapping("/api/solved-problems/judgment-results/submission-code/update/{submissionId}")
    @Operation(summary = "제출 소스 코드 저장,수정 URL 획득")
    public ResponseEntity<UrlResponse> getCodeUpdateUrl(@PathVariable String submissionId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        String url = judgmentService.getCodeUpdateUrl(submissionId, userDetails.getUserId());
        return ResponseEntity.ok(new UrlResponse(url));
    }

    //TODO: 보류 이게 자기가 푼 것 중에 기록이니까... 채점결과를 삭제하는것임
    @DeleteMapping("/api/solved-problems/{solvedProblemId}")
    public ResponseEntity<Void> deleteSolvedProblem(@PathVariable Long solvedProblemId,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        problemService.deleteSolvedProblem(solvedProblemId, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }
}

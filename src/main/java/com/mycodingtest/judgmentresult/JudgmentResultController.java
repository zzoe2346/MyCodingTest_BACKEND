package com.mycodingtest.judgmentresult;

import com.mycodingtest.judgmentresult.dto.JudgmentResultResponse;
import com.mycodingtest.judgmentresult.dto.JudgmentResultSaveRequest;
import com.mycodingtest.security.CustomUserDetails;
import com.mycodingtest.storage.dto.UrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "채점 결과", description = "채점 결과 관련 API")
public class JudgmentResultController {

    private final JudgmentResultService judgmentResultService;

    public JudgmentResultController(JudgmentResultService judgmentResultService) {
        this.judgmentResultService = judgmentResultService;
    }

    @PostMapping("/api/solved-problems/judgment-results")
    @Operation(summary = "채점 결과 저장", description = "크롬 익스텐션으로 부터 전송된 채점 결과를 저장합니다.")
    public ResponseEntity<Void> saveJudgmentResult(@RequestBody JudgmentResultSaveRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        judgmentResultService.saveJudgmentResult(request, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/solved-problems/{solvedProblemId}/judgment-results")
    @Operation(summary = "채점 결과 목록 조회", description = "특정 문제의 채점 결과 목록을 조회합니다.")
    public ResponseEntity<List<JudgmentResultResponse>> getJudgmentResultList(@PathVariable Long solvedProblemId,
                                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(judgmentResultService.getJudgmentResultList(solvedProblemId, userDetails.getUserId()));
    }

    @GetMapping("/api/solved-problems/judgment-results/submission-id-check/{submissionId}")
    @Operation(summary = "이미 제출된 채점 결과인지 확인", description = "특정 채점 결과의 제출 코드를 조회합니다.")
    public ResponseEntity<Void> getDuplicatedSubmissionIdCheckResult(@PathVariable Long submissionId) {
        boolean isDuplicated = judgmentResultService.isSubmissionIdDuplicated(submissionId);
        if (isDuplicated) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/solved-problems/judgment-results/submission-code/read/{submissionId}")
    @Operation(summary = "제출 소스 코드 읽기 URL 획득")
    public ResponseEntity<UrlResponse> getCodeReadUrl(@PathVariable String submissionId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        String url = judgmentResultService.getCodeReadUrl(submissionId, userDetails.getUserId());
        return ResponseEntity.ok(new UrlResponse(url));
    }

    @GetMapping("/api/solved-problems/judgment-results/submission-code/update/{submissionId}")
    @Operation(summary = "제출 소스 코드 저장,수정 URL 획득")
    public ResponseEntity<UrlResponse> getCodeUpdateUrl(@PathVariable String submissionId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        String url = judgmentResultService.getCodeUpdateUrl(submissionId, userDetails.getUserId());
        return ResponseEntity.ok(new UrlResponse(url));
    }
}

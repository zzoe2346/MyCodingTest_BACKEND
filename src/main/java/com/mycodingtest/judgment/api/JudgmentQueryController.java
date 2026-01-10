package com.mycodingtest.judgment.api;

import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.judgment.application.JudgmentService;
import com.mycodingtest.judgment.domain.Judgment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "채점 결과", description = "채점 결과 관련 API")
@RequiredArgsConstructor
public class JudgmentQueryController {

    private final JudgmentService judgmentService;

    //    @GetMapping("/api/solved-problems/{solvedProblemId}/judgment-results")
    @GetMapping("/api/judgments")
    @Operation(summary = "채점 결과 목록 조회", description = "특정 문제의 채점 결과 목록을 조회합니다.")
    public ResponseEntity<List<Judgment>> getJudgmentResultList(
            @RequestBody Long problemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<Judgment> judgments = judgmentService.readJudgments(problemId, userDetails.getUserId());
        return ResponseEntity.ok(judgments);
//        return ResponseEntity.ok(judgments.stream()
//                .map(JudgmentResultMapper::toResponse)
//                .toList()); TODO: 일단 바쁘다 프론트랑 통신하는건 막판에 조정해도 되니 보류
    }

}

package com.mycodingtest.api.judgment;

import com.mycodingtest.application.judgment.JudgmentService;
import com.mycodingtest.security.CustomUserDetails;
import com.mycodingtest.application.judgment.dto.DeleteJudgmentCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "채점 결과", description = "채점 결과 관련 API")
@RequiredArgsConstructor
public class JudgmentCommandController {

    private final JudgmentService judgmentService;

    /**
     * 채점결과 삭제
     *
     * @param judgmentId
     * @param customUserDetails
     * @return
     */
    @DeleteMapping("/api/judgments/{judgmentId}")
    @Operation(summary = "채점 결과 삭제", description = "특정 채점 결과를 삭제합니다.")
    public ResponseEntity<Void> deleteSolvedProblem(@PathVariable Long judgmentId,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        judgmentService.deleteJudgment(DeleteJudgmentCommand.from(judgmentId, customUserDetails.getUserId()));
        return ResponseEntity.ok().build();
    }

}

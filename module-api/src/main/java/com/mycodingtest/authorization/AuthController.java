package com.mycodingtest.authorization;

import com.mycodingtest.authorization.dto.UserInfoResponse;
import com.mycodingtest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "인증", description = "유저의 인증 상태에 관한 API")
@RequiredArgsConstructor
public class AuthController {

    private final CookieUtil cookieUtil;

    @GetMapping("/api/sign-out")
    @Operation(summary = "요청시, 로그아웃이 된다. 발급된 쿠키가 만료되는 로직이 작동함.")
    public ResponseEntity<Void> signOut(HttpServletResponse response) {
        ResponseCookie cookieForClear = cookieUtil.generateClearJwtCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieForClear.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/me")
    @Operation(summary = "요청시, 로그인 상태를 체크한다. 200 응답: 로그인 상태  401 응답: 비로그인 상태이다. DB Hit 없이 쿠키의 유효성을 확인함.")
    public ResponseEntity<UserInfoResponse> checkSignIn(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(new UserInfoResponse(userDetails.getPicture(), userDetails.getUsername()));
    }

}

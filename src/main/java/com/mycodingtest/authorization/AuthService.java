package com.mycodingtest.authorization;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${cookie.name}")
    private String cookieName;

    public void signOut(HttpServletResponse response) {
        ResponseCookie mctApiAccessToken = ResponseCookie.from(cookieName, null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
        ResponseCookie jsessionId = ResponseCookie.from("JSESSIONID", null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, mctApiAccessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jsessionId.toString());
    }

    public UserInfoResponse getUserInfo(String picture, String username) {
        return new UserInfoResponse(picture, username);
    }
}

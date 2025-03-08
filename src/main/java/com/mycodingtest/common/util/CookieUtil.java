package com.mycodingtest.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${cookie.name}")
    private String cookieName;

    public String getJwtFromCookie(HttpServletRequest request) {
        if(request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public ResponseCookie generateJwtCookie(String token) {
        return ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 90)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
    }

    public ResponseCookie generateClearJwtCookie() {
        return ResponseCookie.from(cookieName, null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
    }
}

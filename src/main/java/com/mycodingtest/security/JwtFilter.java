package com.mycodingtest.security;

import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public JwtFilter(JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = cookieUtil.getJwtFromCookie(request);
        if (token != null) {
            setAuthenticationContext(request, token);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(HttpServletRequest request, String token) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = jwtUtil.extractAllClaims(token);

            CustomUserDetails customUserDetails = new CustomUserDetails(claims.get("userId", Long.class), claims.get("picture", String.class), claims.get("name", String.class));

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}

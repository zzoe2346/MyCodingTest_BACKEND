package com.mycodingtest.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CustomUserDetailsTest {

    @Test
    @DisplayName("CustomUserDetails에서 패스워드 게터시 Null이 반환된다")
    void customUserDetailsGetPasswordTest() {
        //given
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "username");

        //when then
        assertThat(customUserDetails.getPassword()).isNull();
    }

    @Test
    @DisplayName("CustomUserDetails에서 Authorities 게터시 정보 ROLE_USER 반환된다")
    void customUserDetailsGetAuthoritiesTest() {
        //given
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "username");

        //when then
        assertThat(customUserDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("CustomUserDetails에서 username 게터시 생성자의 username 반환된다")
    void customUserDetailsGetUsernameTest() {
        //given
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "username");

        //when then
        assertThat(customUserDetails.getUsername()).isEqualTo("username");
    }

    @Test
    @DisplayName("CustomUserDetails에서 userId, picutre 게터시 생성자에서 정한것이 반환된다")
    void customUserDetailsGetterTest() {
        //given
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "username");

        //when then
        assertThat(customUserDetails.getUserId()).isEqualTo(1L);
        assertThat(customUserDetails.getPicture()).isEqualTo("picture");
    }
}

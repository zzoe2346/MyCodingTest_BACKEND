package com.mycodingtest.security;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUserDetailsTest {

    @Nested
    class 사용자_정보_조회 {

        @Test
        void userId를_조회할_수_있다() {
            // given
            Long userId = 1L;
            String picture = "https://example.com/profile.jpg";
            String username = "홍길동";

            // when
            CustomUserDetails userDetails = new CustomUserDetails(userId, picture, username);

            // then
            assertThat(userDetails.getUserId()).isEqualTo(userId);
        }

        @Test
        void picture를_조회할_수_있다() {
            // given
            Long userId = 1L;
            String picture = "https://example.com/profile.jpg";
            String username = "홍길동";

            // when
            CustomUserDetails userDetails = new CustomUserDetails(userId, picture, username);

            // then
            assertThat(userDetails.getPicture()).isEqualTo(picture);
        }

        @Test
        void username을_조회할_수_있다() {
            // given
            Long userId = 1L;
            String picture = "https://example.com/profile.jpg";
            String username = "홍길동";

            // when
            CustomUserDetails userDetails = new CustomUserDetails(userId, picture, username);

            // then
            assertThat(userDetails.getUsername()).isEqualTo(username);
        }
    }

    @Nested
    class UserDetails_인터페이스_구현 {

        @Test
        void password는_null이다() {
            // given
            CustomUserDetails userDetails = new CustomUserDetails(1L, "pic", "user");

            // when
            String password = userDetails.getPassword();

            // then
            assertThat(password).isNull();
        }

        @Test
        void authorities에_ROLE_USER가_포함되어_있다() {
            // given
            CustomUserDetails userDetails = new CustomUserDetails(1L, "pic", "user");

            // when
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            // then
            assertThat(authorities).hasSize(1);
            assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");
        }

        @Test
        void 계정_상태_메서드들은_기본값_true를_반환한다() {
            // given
            CustomUserDetails userDetails = new CustomUserDetails(1L, "pic", "user");

            // when & then
            assertThat(userDetails.isAccountNonExpired()).isTrue();
            assertThat(userDetails.isAccountNonLocked()).isTrue();
            assertThat(userDetails.isCredentialsNonExpired()).isTrue();
            assertThat(userDetails.isEnabled()).isTrue();
        }
    }
}

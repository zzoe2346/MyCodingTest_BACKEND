package com.mycodingtest.application.user;

import com.mycodingtest.application.user.command.SyncUserCommand;
import com.mycodingtest.application.user.command.UserCommandService;
import com.mycodingtest.domain.user.User;
import com.mycodingtest.domain.user.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCommandService userCommandService;

    @Nested
    class 사용자_생성_또는_조회 {

        @Test
        void 사용자_정보로_User를_생성하고_저장한다() {
            // given
            String name = "홍길동";
            String email = "hong@example.com";
            String picture = "https://example.com/profile.jpg";
            String provider = "google";
            String oauthId = "google-12345";

            User savedUser = User.from(1L, name, email, picture, provider, oauthId);
            given(userRepository.save(any(User.class))).willReturn(savedUser);

            // when
            User result = userCommandService.syncUser(SyncUserCommand.from(name, email, picture, provider, oauthId));

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getEmail()).isEqualTo(email);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(captor.capture());

            User capturedUser = captor.getValue();
            assertThat(capturedUser.getName()).isEqualTo(name);
            assertThat(capturedUser.getOauthProvider()).isEqualTo(provider);
            assertThat(capturedUser.getOauthId()).isEqualTo(oauthId);
        }

        @Test
        void 다른_OAuth_제공자로_사용자를_생성할_수_있다() {
            // given
            String name = "카카오사용자";
            String email = "kakao@example.com";
            String picture = "https://kakao.com/profile.jpg";
            String provider = "kakao";
            String oauthId = "kakao-67890";

            User savedUser = User.from(2L, name, email, picture, provider, oauthId);
            given(userRepository.save(any(User.class))).willReturn(savedUser);

            // when
            User result = userCommandService.syncUser(SyncUserCommand.from(name, email, picture, provider, oauthId));

            // then
            assertThat(result.getOauthProvider()).isEqualTo("kakao");
            assertThat(result.getOauthId()).isEqualTo("kakao-67890");
        }
    }
}

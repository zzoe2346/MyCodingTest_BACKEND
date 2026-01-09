package com.mycodingtest.user;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.user.application.UserService;
import com.mycodingtest.user.domain.User;
import com.mycodingtest.user.domain.UserRepository;
import com.mycodingtest.user.dto.UserDetailInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Tag("unit")
@MockitoSettings
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("OAuth 제공자와 ID로 사용자를 조회한다")
    void findByOauthProviderAndOauthId() {
        // given
        String provider = "google";
        String oauthId = "123456";
        User expectedUser = new User("Test User", "test@test.com", "test-picture", provider, oauthId);
        given(userRepository.findByOauthProviderAndOauthId(provider, oauthId))
                .willReturn(Optional.of(expectedUser));

        // when
        Optional<User> foundUser = userService.findByOauthProviderAndOauthId(provider, oauthId);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getOauthProvider()).isEqualTo(provider);
        assertThat(foundUser.get().getOauthId()).isEqualTo(oauthId);
    }

    @Test
    @DisplayName("존재하지 않는 OAuth 사용자 조회 시 empty Optional을 반환한다")
    void findByOauthProviderAndOauthId_WhenUserNotExists() {
        // given
        String provider = "google";
        String oauthId = "non-exist";
        given(userRepository.findByOauthProviderAndOauthId(provider, oauthId))
                .willReturn(Optional.empty());

        // when
        Optional<User> foundUser = userService.findByOauthProviderAndOauthId(provider, oauthId);

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("신규 사용자를 등록한다")
    void enrollNewUser() {
        // given
        String name = "testUser";
        String email = "test@test.com";
        String picture = "testPicture";
        String provider = "google";
        String oauthId = "123456";

        User savedUser = new User(name, email, picture, provider, oauthId);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        User enrolledUser = userService.enrollNewUser(name, email, picture, provider, oauthId);

        // then
        assertThat(enrolledUser.getName()).isEqualTo(name);
        assertThat(enrolledUser.getEmail()).isEqualTo(email);
        assertThat(enrolledUser.getPicture()).isEqualTo(picture);
        assertThat(enrolledUser.getOauthProvider()).isEqualTo(provider);
        assertThat(enrolledUser.getOauthId()).isEqualTo(oauthId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 상세 정보를 조회한다")
    void getUserDetailInfo() {
        // given
        Long userId = 1L;

        User user = new User("testUser", "test@test.com", "testPicture", "google", "123456");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserDetailInfoResponse response = userService.getUserDetailInfo(userId);

        // then
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.picture()).isEqualTo(user.getPicture());
        assertThat(response.oauthProvider()).isEqualTo(user.getOauthProvider());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회시 예외가 발생한다")
    void getUserDetailInfo_WhenUserNotFound() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when then
        assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserDetailInfo(userId));
    }
}
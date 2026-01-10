package com.mycodingtest.user.application;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.user.domain.User;
import com.mycodingtest.user.domain.UserRepository;
import com.mycodingtest.user.dto.UserDetailInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void OAuth_제공자와_ID로_유저_조회_성공() {
        // given
        String provider = "google";
        String oauthId = "12345";
        User user = new User("name", "email", "pic", provider, oauthId);
        given(userRepository.findByOauthProviderAndOauthId(provider, oauthId))
                .willReturn(Optional.of(user));

        // when
        Optional<User> result = userService.findByOauthProviderAndOauthId(provider, oauthId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void 신규_유저_등록_성공() {
        // given
        String name = "New User";
        String email = "new@test.com";
        String picture = "pic_url";
        String provider = "google";
        String oauthId = "12345";
        User savedUser = new User(name, email, picture, provider, oauthId);

        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        User result = userService.enrollNewUser(name, email, picture, provider, oauthId);

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getEmail()).isEqualTo(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void 유저_상세_정보_조회_성공() {
        // given
        Long userId = 1L;
        User user = new User("name", "email", "pic", "google", "123");
        // Reflection or setter needed if ID is used, but Mapper might use other fields.
        // Assuming Mapper maps fields correctly.
        
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserDetailInfoResponse result = userService.getUserDetailInfo(userId);

        // then
        assertThat(result).isNotNull();
        // Add more assertions based on UserMapper logic if known, e.g. name check
        assertThat(result.name()).isEqualTo("name");
    }

    @Test
    void 존재하지_않는_유저_상세_정보_조회_실패() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserDetailInfo(userId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

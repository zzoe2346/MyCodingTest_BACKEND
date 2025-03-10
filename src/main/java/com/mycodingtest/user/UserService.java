package com.mycodingtest.user;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.user.dto.UserDetailInfoResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByOauthProviderAndOauthId(String provider, String oauthId) {
        return userRepository.findByOauthProviderAndOauthId(provider, oauthId);
    }

    public User enrollNewUser(String name, String email, String picture, String provider, String oauthId) {
        return userRepository.save(new User(name, email, picture, provider, oauthId));
    }

    public UserDetailInfoResponse getUserDetailInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
        return UserMapper.toDetailInfoResponse(user);
    }
}

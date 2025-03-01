package com.mycodingtest.user;

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
}

package com.mycodingtest.infra.user;

import com.mycodingtest.domain.user.User;
import com.mycodingtest.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository repository;

    @Override
    public Optional<User> findUser(String oauthProvider, String oauthId) {
        return repository.findByOauthProviderAndOauthId(oauthProvider, oauthId)
                .map(UserEntity::toDomain);
    }

    @Override
    public User save(User user) {
        return repository.save(UserEntity.from(user)).toDomain();
    }

}

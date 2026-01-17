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
    private final UserMapper mapper;

    @Override
    public Optional<User> findUser(String oauthProvider, String oauthId) {
        return repository.findByOauthProviderAndOauthId(oauthProvider, oauthId)
                .map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }

}

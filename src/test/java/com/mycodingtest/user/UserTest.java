package com.mycodingtest.user;

import com.mycodingtest.user.domain.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
class UserTest {

    @Test
    void testNoArgConstructor() {
        // when
        User user = new User();

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
        assertThat(user.getOauthProvider()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getEmail()).isNull();
    }

    @Test
    void testParamArgConstructor() {
        // when
        User user = new User("Bell", "test@test.com", "picture", "google", "1122");

        // then
        // @CreationTimestamp 은 데이터베이스에 insert의 시간을 기록. 단위테스트에서는 null이 정상
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("Bell");
        assertThat(user.getOauthProvider()).isEqualTo("google");
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@test.com");
    }
}

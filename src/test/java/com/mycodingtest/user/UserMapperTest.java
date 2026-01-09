package com.mycodingtest.user;


import com.mycodingtest.user.domain.User;
import com.mycodingtest.user.dto.UserDetailInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Tag("unit")
class UserMapperTest {

    @Test
    @DisplayName("UserMapper는 유틸리티성 클래스이므로 인스턴스 생성이 불가능하다")
    void testPrivateConstructor() throws Exception {
        //given
        Constructor<UserMapper> constructor = UserMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        //when then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }


    @Test
    @DisplayName("User 엔티티를 UserDetailInfoResponse로 매핑한다")
    void toDetailInfoResponse_Test() {
        //given
        User user = mock(User.class);
        given(user.getId()).willReturn(1L);
        given(user.getName()).willReturn("name");
        given(user.getPicture()).willReturn("pic");
        given(user.getEmail()).willReturn("email");
        given(user.getOauthProvider()).willReturn("provider");
        given(user.getOauthId()).willReturn("oauthId");

        //when

        UserDetailInfoResponse userDetailInfoResponse = UserMapper.toDetailInfoResponse(user);

        //then
        assertThat(userDetailInfoResponse).isNotNull();
        assertThat(userDetailInfoResponse.name()).isEqualTo("name");
    }
}

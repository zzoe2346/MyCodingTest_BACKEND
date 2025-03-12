package com.mycodingtest.judgmentresult;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
class JudgmentResultMapperTest {

    @Test
    @DisplayName("JudgmentResultMapper는 유틸리티성 클래스이므로 인스턴스 생성이 불가능하다")
    void testPrivateConstructor() throws Exception {
        //given
        Constructor<JudgmentResultMapper> constructor = JudgmentResultMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        //when then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

}

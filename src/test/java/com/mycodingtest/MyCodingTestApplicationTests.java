package com.mycodingtest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyCodingTestApplicationTests {

    @Test
    @Tag("unit")
    void contextLoads() {
        MyCodingTestApplication.main(new String[]{});
    }

}

package com.mycodingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyCodingTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyCodingTestApplication.class, args);
    }

}

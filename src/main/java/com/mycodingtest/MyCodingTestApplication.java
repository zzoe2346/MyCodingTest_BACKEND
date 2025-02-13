package com.mycodingtest;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class MyCodingTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyCodingTestApplication.class, args);
    }

}

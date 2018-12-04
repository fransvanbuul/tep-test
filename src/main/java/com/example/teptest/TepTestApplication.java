package com.example.teptest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TepTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TepTestApplication.class, args);
    }
}

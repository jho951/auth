package com.auth.sample.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SampleJwtApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleJwtApiApplication.class, args);
    }

    @RestController
    static class HealthController {

        @GetMapping("/ping")
        public String ping() {
            return "jwt sample up";
        }
    }
}

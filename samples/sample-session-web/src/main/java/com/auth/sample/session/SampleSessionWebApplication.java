package com.auth.sample.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SampleSessionWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleSessionWebApplication.class, args);
    }

    @RestController
    static class HealthController {

        @GetMapping("/session-ping")
        public String ping() {
            return "session sample up";
        }
    }
}

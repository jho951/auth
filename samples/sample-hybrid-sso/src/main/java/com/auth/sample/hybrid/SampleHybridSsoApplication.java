package com.auth.sample.hybrid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SampleHybridSsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleHybridSsoApplication.class, args);
    }

    @RestController
    static class HealthController {

        @GetMapping("/hybrid-ping")
        public String ping() {
            return "hybrid sample up";
        }
    }
}

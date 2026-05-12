package com.kuldeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    @GetMapping("/api/health")
    public String healthCheck() {
        return "{\"status\": \"UP\", \"service\": \"spring-llm-app\"}";
    }
    
    @GetMapping("/api/review")
    public String codeReview() {
        return "{\"status\": \"AI Review Ready\", \"model\": \"StarCoder\"}";
    }
}
    // PR test trigger

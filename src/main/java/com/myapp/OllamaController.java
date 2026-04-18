package com.myapp;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class OllamaController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String OLLAMA_URL = "http://localhost:11434/api/generate";

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "spring-llm-app");
    }

    @PostMapping("/review")
    public Map<String, Object> reviewCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        Map<String, Object> ollamaRequest = new HashMap<>();
        ollamaRequest.put("model", "llama3.2:3b");
        ollamaRequest.put("prompt", "Review this Java code and find bugs, security issues:\n" + code);
        ollamaRequest.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(ollamaRequest, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OLLAMA_URL, entity, Map.class);

        return Map.of(
            "review", response.getBody().get("response"),
            "status", "success"
        );
    }
}
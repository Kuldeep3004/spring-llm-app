package com.kuldeep;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class OllamaController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String OLLAMA_URL = "http://localhost:11434/api/generate";

@GetMapping("/vulnerable")
public String vulnerable(@RequestParam String userInput) {

    String query = "SELECT * FROM users WHERE id=" + userInput;

    return "Potentially vulnerable query: " + query;
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
                "status", "success");
    }
}
// PR demo change
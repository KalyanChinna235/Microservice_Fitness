package com.fitness.aiservice.config;

import com.fitness.aiservice.dto.AskRequest;
import com.fitness.aiservice.dto.GeminiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient geminiWebClient;

    @Value("${gemini.api.key}")
    private String apiKey;


    public String generateContent(String prompt) {

        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt must not be empty");
        }

        GeminiRequest.Part part = new GeminiRequest.Part(prompt);
        GeminiRequest.Content content = new GeminiRequest.Content(List.of(part));
        GeminiRequest request = new GeminiRequest(List.of(content));

        Map response = geminiWebClient.post()
                .uri("/models/gemini-flash-latest:generateContent")
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", apiKey)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .map(errorBody ->
                                        new RuntimeException("Gemini API Error: " + errorBody)
                                )
                )
                .bodyToMono(Map.class)
                .block();

        List candidates = (List) response.get("candidates");
        Map firstCandidate = (Map) candidates.get(0);
        Map contentMap = (Map) firstCandidate.get("content");
        List parts = (List) contentMap.get("parts");
        Map textPart = (Map) parts.get(0);

        return (String) textPart.get("text");
    }
}
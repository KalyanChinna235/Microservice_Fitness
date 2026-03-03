package com.fitness.aiservice.config;

import com.fitness.aiservice.dto.GeminiRequest;
import com.fitness.aiservice.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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

        GeminiResponse response = geminiWebClient.post()
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
                .bodyToMono(GeminiResponse.class)
                .block();

        if (response == null
                || response.getCandidates() == null
                || response.getCandidates().isEmpty()) {
            throw new RuntimeException("Invalid Gemini response");
        }
        return response.getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();
    }
}
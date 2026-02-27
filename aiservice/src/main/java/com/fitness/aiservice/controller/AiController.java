package com.fitness.aiservice.controller;

import com.fitness.aiservice.config.GeminiClient;
import com.fitness.aiservice.dto.AskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final GeminiClient geminiClient;

    @GetMapping("/ask")
    public ResponseEntity<String> ask(@RequestParam String prompt) {
        return new ResponseEntity<>(geminiClient.generateContent(prompt), HttpStatus.OK);
    }
}
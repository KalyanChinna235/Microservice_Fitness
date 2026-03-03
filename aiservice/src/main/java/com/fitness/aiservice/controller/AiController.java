package com.fitness.aiservice.controller;

import com.fitness.aiservice.dto.AskRequest;
import com.fitness.aiservice.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody AskRequest request) {
        String result = aiService.askChat(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
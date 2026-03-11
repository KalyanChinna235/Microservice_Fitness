package com.fitness.aiservice.controller;

import com.fitness.aiservice.dto.RecommendationResponse;
import com.fitness.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getUserRecommendations(@PathVariable String userId) {

        List<RecommendationResponse> userRecommendations = recommendationService.getUserRecommendations(userId);
        return ResponseEntity.ok().body(userRecommendations);

    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<RecommendationResponse> getActivityRecommendation(@PathVariable String activityId) {
        return ResponseEntity.ok().body(recommendationService.getActivityRecommendation(activityId));
    }
}

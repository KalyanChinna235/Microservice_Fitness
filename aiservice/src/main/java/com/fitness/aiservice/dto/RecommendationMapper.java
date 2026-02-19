package com.fitness.aiservice.dto;

import com.fitness.aiservice.entity.Recommendation;

public class RecommendationMapper {

    public static RecommendationResponse toResponse(Recommendation recommendation) {
        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .activityId(recommendation.getActivityId())
                .userId(recommendation.getUserId())
                .activityType(recommendation.getActivityType())
                .recommendation(recommendation.getRecommendation())
                .improvements(recommendation.getImprovements())
                .suggestions(recommendation.getSuggestions())
                .safety(recommendation.getSafety())
                .createdAt(recommendation.getCreatedAt())
                .build();
    }

    public static Recommendation toEntity(RecommendationResponse response) {
        return Recommendation.builder()
                .id(response.getId())
                .activityId(response.getActivityId())
                .userId(response.getUserId())
                .activityType(response.getActivityType())
                .recommendation(response.getRecommendation())
                .improvements(response.getImprovements())
                .suggestions(response.getSuggestions())
                .safety(response.getSafety())
                .createdAt(response.getCreatedAt())
                .build();
    }
}

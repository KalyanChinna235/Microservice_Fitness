package com.fitness.aiservice.service;

import com.fitness.aiservice.dto.RecommendationResponse;
import com.fitness.aiservice.entity.Recommendation;
import com.fitness.aiservice.exception.ResourceNotFoundException;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public List<RecommendationResponse> getUserRecommendations(String userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUserId(userId);
        if (recommendations.isEmpty()) {
            throw new ResourceNotFoundException("No recommendations found for user with ID " + userId);
        }
        return recommendations.stream()
                .map(rec -> RecommendationResponse.builder()
                        .id(rec.getId())
                        .activityId(rec.getActivityId())
                        .userId(rec.getUserId())
                        .activityType(rec.getActivityType())
                        .recommendation(rec.getRecommendation())
                        .improvements(rec.getImprovements())
                        .suggestions(rec.getSuggestions())
                        .safety(rec.getSafety())
                        .createdAt(rec.getCreatedAt())
                        .build())
                .toList();
    }

    public RecommendationResponse getActivityRecommendation(String activityId) {
        Optional<Recommendation> recommendations = recommendationRepository.findByActivityId(activityId);
        if (recommendations.isEmpty()) {
            throw new ResourceNotFoundException("No recommendation found for activity with ID " + activityId);
        } else {
            return RecommendationResponse.builder()
                    .id(recommendations.get().getId())
                    .activityId(recommendations.get().getActivityId())
                    .userId(recommendations.get().getUserId())
                    .activityType(recommendations.get().getActivityType())
                    .recommendation(recommendations.get().getRecommendation())
                    .improvements(recommendations.get().getImprovements())
                    .suggestions(recommendations.get().getSuggestions())
                    .safety(recommendations.get().getSafety())
                    .createdAt(recommendations.get().getCreatedAt())
                    .build();
        }
    }
}

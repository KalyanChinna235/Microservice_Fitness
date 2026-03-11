package com.fitness.aiservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiRecommendationResponse {

    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;
}

package com.fitness.aiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

    private Long id;

    private Long activityId;
    private Long userId;
    private String activityType;

    private String recommendation;

    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;

    private LocalDateTime createdAt;

}

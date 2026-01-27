package com.fitness.activity.dto;

import com.fitness.activity.entity.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {

    private Long id;
    private Long userId;
    private ActivityType type;
    private Integer duration; //
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

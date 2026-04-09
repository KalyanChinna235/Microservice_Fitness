package com.fitness.activity.service;

import com.fitness.activity.dto.ActivityRequest;
import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.entity.Activity;
import com.fitness.activity.exception.ResourceNotFoundException;
import com.fitness.activity.repo.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // Track Activity
    public ActivityResponse trackActivity(ActivityRequest activityRequest) {

        // Extract keycloakId from JWT
        String keycloakId = getKeycloakIdFromToken();

        // Get userId from USER-SERVICE
        Long userId = userValidationService.getUserIdByKeycloakId(keycloakId);

        if (userId == null) {
            throw new ResourceNotFoundException("User not found");
        }

        // Save activity
        Activity activity = Activity.builder()
                .userId(userId)
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();

        Activity saved = activityRepository.save(activity);

        // Publish event
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, saved);
        } catch (Exception e) {
            log.error("RabbitMQ error: {}", e.getMessage());
        }

        return toMapResponse(saved);
    }

    // Get all activities for logged-in user
    public List<ActivityResponse> getUserActivities() {

        String keycloakId = getKeycloakIdFromToken();

        Long userId = userValidationService.getUserIdByKeycloakId(keycloakId);

        if (userId == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return activityRepository.findByUserId(userId)
                .stream()
                .map(this::toMapResponse)
                .toList();
    }

    // Get single activity
    public Activity getUserActivity(Long activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isEmpty()) {
            throw new ResourceNotFoundException("Activity not found");
        }
        return activity.get();
    }

    // Mapper
    public ActivityResponse toMapResponse(Activity activity) {
        ActivityResponse res = new ActivityResponse();
        res.setId(activity.getId());
        res.setUserId(activity.getUserId());
        res.setType(activity.getType());
        res.setDuration(activity.getDuration());
        res.setCaloriesBurned(activity.getCaloriesBurned());
        res.setStartTime(activity.getStartTime());
        res.setAdditionalMetrics(activity.getAdditionalMetrics());
        return res;
    }

    // Common method (BEST PRACTICE)
    private String getKeycloakIdFromToken() {
        Jwt jwt = (Jwt) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return jwt.getSubject();
    }
}
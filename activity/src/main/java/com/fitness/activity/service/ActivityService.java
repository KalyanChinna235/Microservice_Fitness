package com.fitness.activity.service;

import com.fitness.activity.dto.ActivityRequest;
import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.entity.Activity;
import com.fitness.activity.exception.ResourceNotFoundException;
import com.fitness.activity.repo.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final UserValidationService userValidationService;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        boolean userExist = userValidationService.validateUserExists(activityRequest.getUserId());
        if(!userExist) {
            throw new ResourceNotFoundException("User with id " + activityRequest.getUserId() + " not found");
        }
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        Activity activitySave = activityRepository.save(activity);
        return toMapResponse(activitySave);
    }

    public ActivityResponse toMapResponse(Activity activity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        return activityResponse;

    }

    public List<ActivityResponse> getUserActivities(Long userId) {

        boolean userExist = activityRepository.existsByUserId(userId);
        if (!userExist) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }
        return activityRepository.findByUserId(userId)
                .stream()
                .map(this::toMapResponse)
                .toList();
    }

    public Activity getUserActivity(Long activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isEmpty()) {
            throw new ResourceNotFoundException(("Activity with id " + activityId + " not found"));
        }
        return activity.get();
    }
}

package com.fitness.activity.controller;

import com.fitness.activity.dto.ActivityRequest;
import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.entity.Activity;
import com.fitness.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/track")
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest activityRequest) {

        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }

    @GetMapping()
    public ResponseEntity<List<ActivityResponse>> getActivitys(@RequestHeader(name = "X-User-Id") Long userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));

    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable Long activityId) {
        Activity activity = activityService.getUserActivity(activityId);
        ActivityResponse activityResponse = activityService.toMapResponse(activity);
        return ResponseEntity.ok(activityResponse);
    }
}

package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;

import java.util.List;

public interface ActivityService {
    ActivityResponse trackActivity(ActivityRequest request);

    List<ActivityResponse> getAllActivities();

    ActivityResponse getActivityById(String id);
}

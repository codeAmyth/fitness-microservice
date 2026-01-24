package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ActivityRepository activityRepository;

    private final UserValidationService userValidationService;


    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValid = userValidationService.validateUser(request.getUserId());

        if (!isValid) {
            throw new RuntimeException("Invalid User: " + request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        ActivityResponse activityResponse = modelMapper.map(savedActivity, ActivityResponse.class);

        return activityResponse;

    }
}

package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ActivityRepository activityRepository;
    @Value("${kafka.topic.name}")
    private String topicName;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {

        if (request.getUserId() == null || request.getUserId().isBlank()) {
            throw new IllegalArgumentException("UserId is missing in ActivityRequest");
        }

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
        kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);

        return modelMapper.map(savedActivity, ActivityResponse.class);
    }

    @Override
    public List<ActivityResponse> getAllActivities() {
        return activityRepository.findAll()
                .stream()
                .map(activity -> modelMapper.map(activity, ActivityResponse.class))
                .toList();
    }

    @Override
    public ActivityResponse getActivityById(String id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        return modelMapper.map(activity, ActivityResponse.class);
    }
}

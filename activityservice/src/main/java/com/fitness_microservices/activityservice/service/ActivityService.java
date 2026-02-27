package com.fitness_microservices.activityservice.service;


import com.fitness_microservices.activityservice.dto.ActivityRequest;
import com.fitness_microservices.activityservice.dto.ActivityResponse;
import com.fitness_microservices.activityservice.model.Activity;
import com.fitness_microservices.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final UserValidationService userValidationService;

    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if(!isValidUser){
            throw new RuntimeException("Invalid User:" + request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .Duration((request.getDuration()))
                .caloriesBurned(request.getCaloriesBurned())
                .start(request.getStart())
                .end(request.getEnd())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
try {
    kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
}catch (Exception e){
    e.printStackTrace();
}
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity savedActivity) {
        ActivityResponse response = new ActivityResponse();
                response.setId(savedActivity.getId());
                response.setUserId(savedActivity.getUserId());
                response.setType(savedActivity.getType());
                response.setDuration(savedActivity.getDuration());
                response.setCaloriesBurned(savedActivity.getCaloriesBurned());
                response.setStart(savedActivity.getStart());
                response.setEnd(savedActivity.getEnd());
                response.setAdditionalMetrics(savedActivity.getAdditionalMetrics());

                return response;
    }


    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activityList = activityRepository.findByUserId(userId);
        return activityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}

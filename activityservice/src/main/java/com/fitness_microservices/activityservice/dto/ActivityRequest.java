package com.fitness_microservices.activityservice.dto;


import com.fitness_microservices.activityservice.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {


    private String userId;
    private ActivityType type;
    private Integer Duration;
    private Integer caloriesBurned;
    private LocalDateTime start;
    private LocalDateTime end;
    private Map<String, Object> additionalMetrics;

}

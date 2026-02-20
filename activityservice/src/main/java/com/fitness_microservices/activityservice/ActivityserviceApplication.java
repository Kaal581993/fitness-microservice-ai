package com.fitness_microservices.activityservice;

import com.fitness_microservices.activityservice.repository.ActivityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivityserviceApplication {

	public static void main(String[] args) {


        SpringApplication.run(ActivityserviceApplication.class, args);
	}

}

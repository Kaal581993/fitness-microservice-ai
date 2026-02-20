package com.fitness_microservices.userservice.dto;

import com.fitness_microservices.userservice.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String userID;
    private String firstName;
    private String lastName;
    private Date DOB;
    private String username;
    private String email;
    private String password;
    private UserRole role = UserRole.USER;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

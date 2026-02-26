package com.example.gateway.userValidationKeycloak;

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
    private String keycloakId;
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

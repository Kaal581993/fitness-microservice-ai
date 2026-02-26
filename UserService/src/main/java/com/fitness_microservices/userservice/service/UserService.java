package com.fitness_microservices.userservice.service;


import com.fitness_microservices.userservice.dto.RegisterRequest;
import com.fitness_microservices.userservice.dto.UserResponse;
import com.fitness_microservices.userservice.models.User;
import com.fitness_microservices.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private  final UserRepository repository;
    public UserResponse register(RegisterRequest request) {

        if(repository.existsByEmail(request.getEmail())){
            User existingUser = repository.findByEmail(request.getEmail());

            UserResponse userResponse = new UserResponse();
            userResponse.setUserID(existingUser.getUserID());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setDOB(existingUser.getDOB());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setPassword(existingUser.getPassword());

            return userResponse;
        }
        User user = new User();
        user.setKeyCloakId(request.getKeyCloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setDOB(request.getDOB());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = repository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setUserID(savedUser.getUserID());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setDOB(savedUser.getDOB());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());

        return userResponse;
    }

    public UserResponse getUserProfile(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setUserID(user.getUserID());
        userResponse.setKeyCloakId(user.getKeyCloakId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setDOB(user.getDOB());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());

        return userResponse;

    }

    public  Boolean existByUserId(String userId) {
        log.info("Calling User Service for {}", userId);
        return repository.existsBykeyCloakId(userId);
    }
}

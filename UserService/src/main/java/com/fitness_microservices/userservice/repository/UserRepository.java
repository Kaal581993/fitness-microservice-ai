package com.fitness_microservices.userservice.repository;

import com.fitness_microservices.userservice.models.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsByEmail(String email);

    Boolean existsBykeyCloakId(String userId);

    User findByEmail(@NotBlank(message = "Email is required") String email);
}

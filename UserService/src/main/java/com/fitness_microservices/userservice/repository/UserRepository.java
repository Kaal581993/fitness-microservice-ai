package com.fitness_microservices.userservice.repository;

import com.fitness_microservices.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsByEmail(String email);
}

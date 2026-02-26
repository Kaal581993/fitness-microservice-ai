package com.fitness_microservices.userservice.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userID;


    private String keyCloakId;
    private String firstName;
    private String lastName;

    private Date DOB;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @CreationTimestamp
    @Column(updatable = false, unique = true, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(updatable = false, unique = true, nullable = false)
    private LocalDateTime updatedAt;

}

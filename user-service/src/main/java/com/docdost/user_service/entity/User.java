package com.docdost.user_service.entity;

import com.docdost.user_service.enums.GlobalUserRoles;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email_id", unique=true, nullable=false)
    private String email;

    @Column(name="username", unique=true, nullable=false)
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private GlobalUserRoles role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
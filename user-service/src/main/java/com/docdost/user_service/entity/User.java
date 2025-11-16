package com.docdost.user_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "email_id", unique=true, nullable=false)
    private String email;

    @Column(name="username", unique=true, nullable=false)
    private String username;

    @Column(name="password")
    private String password;
}
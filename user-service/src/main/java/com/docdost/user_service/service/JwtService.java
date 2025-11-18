package com.docdost.user_service.service;

import com.docdost.user_service.entity.User;

import java.util.Date;
import java.util.UUID;

public interface JwtService {
    String generateToken(User user);
    String generateRefreshToken(User user);
    boolean valiateToken(String token);
    UUID extractUserId(String token);
    String extractEmail(String token);
    String extractUsername(String token);
}

package com.docdost.user_service.Service.ServiceImpl;

import com.docdost.user_service.Entity.User;

import java.util.Date;

public interface JwtService {
    String generateToken(User user);
    String generateRefreshToken(User user);
    boolean valiateToken(String token);
    boolean extractUserId(String token);
    String extractEmail(String token);
    Date extractExpiration(String token);
}

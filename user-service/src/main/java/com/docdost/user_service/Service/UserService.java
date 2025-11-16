package com.docdost.user_service.Service;

import com.docdost.user_service.DTO.responses.UserResponse;

import java.util.UUID;

public interface UserService {
    void updateUsername(UUID userId, String newUsername);
    void updatePassword(UUID userId, String newPassword);
    UserResponse getUser(UUID userId);
}


package com.docdost.user_service.service;

import com.docdost.user_service.dto.requests.EmailUpdateRequest;
import com.docdost.user_service.dto.requests.PasswordUpdateRequest;
import com.docdost.user_service.dto.requests.UsernameUpdateRequest;
import com.docdost.user_service.dto.responses.RoleUpdateResponse;
import com.docdost.user_service.dto.responses.UserResponse;
import com.docdost.user_service.entity.User;
import com.docdost.user_service.enums.GlobalUserRoles;

import java.util.UUID;

public interface UserService {
    UserResponse getUser(UUID userId);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    UserResponse updateUsername(UUID userId, UsernameUpdateRequest request);
    UserResponse updatePassword(UUID userId, PasswordUpdateRequest request);
    UserResponse updateEmail(UUID userId, EmailUpdateRequest request);
    RoleUpdateResponse updateRole(UUID userId, GlobalUserRoles newRole);
    //To-do
//    void deleteUser(UUID userId);
}


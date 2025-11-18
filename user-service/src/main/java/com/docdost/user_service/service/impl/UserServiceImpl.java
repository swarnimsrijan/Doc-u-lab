package com.docdost.user_service.service.impl;

import com.docdost.user_service.dto.requests.EmailUpdateRequest;
import com.docdost.user_service.dto.requests.PasswordUpdateRequest;
import com.docdost.user_service.dto.requests.UsernameUpdateRequest;
import com.docdost.user_service.dto.responses.RoleUpdateResponse;
import com.docdost.user_service.dto.responses.UserResponse;
import com.docdost.user_service.entity.User;
import com.docdost.user_service.enums.GlobalUserRoles;
import com.docdost.user_service.repository.UserRepository;
import com.docdost.user_service.service.UserService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse updateUsername(UUID userId, UsernameUpdateRequest request) {
        return null;
    }

    @Override
    public UserResponse updatePassword(UUID userId, PasswordUpdateRequest request) {
        return null;
    }

    @Override
    public UserResponse getUser(UUID userId) {
        return null;
    }

    @Override
    public User getUserByEmail(String email){
        return null;
    }

    @Override
    public User getUserByUsername(String username){
        return null;
    }

    @Override
    public UserResponse updateEmail(UUID userId, EmailUpdateRequest emailUpdateRequest){
        return null;
    }

    @Override
    public RoleUpdateResponse updateRole(UUID userId, GlobalUserRoles newRole){
        return null;
    }
}

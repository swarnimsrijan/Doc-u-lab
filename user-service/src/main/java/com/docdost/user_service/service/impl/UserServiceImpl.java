package com.docdost.user_service.service.serviceImpl;

import com.docdost.user_service.dto.responses.UserResponse;
import com.docdost.user_service.entity.User;
import com.docdost.user_service.repository.UserRepository;
import com.docdost.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateUsername(UUID userId, String newUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserResponse getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail());
    }
}

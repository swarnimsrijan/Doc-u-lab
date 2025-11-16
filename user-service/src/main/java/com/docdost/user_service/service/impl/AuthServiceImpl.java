package com.docdost.user_service.service.serviceImpl;

import com.docdost.user_service.dto.requests.LogInRequest;
import com.docdost.user_service.dto.requests.RegisterRequest;
import com.docdost.user_service.repository.UserRepository;
import com.docdost.user_service.service.AuthService;
import com.docdost.user_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String register(RegisterRequest request) {
        // register logic
    }

    @Override
    public String login(LogInRequest request) {
        // login logic
    }
}

package com.docdost.user_service.Service.ServiceImpl;

import com.docdost.user_service.DTO.requests.LogInRequest;
import com.docdost.user_service.DTO.requests.RegisterRequest;
import com.docdost.user_service.Repository.UserRepository;
import com.docdost.user_service.Service.AuthService;
import com.docdost.user_service.Service.JwtService;
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

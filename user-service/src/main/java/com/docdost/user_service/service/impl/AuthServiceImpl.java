package com.docdost.user_service.service.impl;

import com.docdost.user_service.dto.requests.LogInRequest;
import com.docdost.user_service.dto.requests.RegisterRequest;
import com.docdost.user_service.dto.requests.ResetPasswordRequest;
import com.docdost.user_service.dto.responses.AuthResponse;
import com.docdost.user_service.dto.responses.UserAvailabilityResponse;
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
    public AuthResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LogInRequest request) {
        return null;
    }

    @Override
    public AuthResponse refreshToken(String refreshToken){
        return null;
    }

    @Override
    public void forgotPassword(String email){

    }

    @Override
    public void resetPassword(ResetPasswordRequest request){

    }

    @Override
    public UserAvailabilityResponse checkUsernameAvailability(String username){
        return null;
    }

}

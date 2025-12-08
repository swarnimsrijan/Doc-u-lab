package com.docdost.user_service.service;

import com.docdost.user_service.dto.requests.LogInRequest;
import com.docdost.user_service.dto.requests.RegisterRequest;
import com.docdost.user_service.dto.requests.ResetPasswordRequest;
import com.docdost.user_service.dto.responses.AuthResponse;
import com.docdost.user_service.dto.responses.UserAvailabilityResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LogInRequest request);
    AuthResponse refreshToken(String refreshToken);
    void forgotPassword(String email);
    void resetPassword(ResetPasswordRequest request);
    UserAvailabilityResponse checkUsernameAvailability(String username);
    // To-do
//    AuthResponse googleLogin(String googleToken); // for SSO
}

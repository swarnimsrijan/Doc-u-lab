package com.docdost.user_service.service;

import com.docdost.user_service.dto.requests.LogInRequest;
import com.docdost.user_service.dto.requests.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    String login(LogInRequest request);
}

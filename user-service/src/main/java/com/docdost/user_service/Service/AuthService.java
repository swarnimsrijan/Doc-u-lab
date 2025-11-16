package com.docdost.user_service.Service.ServiceImpl;

import com.docdost.user_service.DTO.requests.LogInRequest;
import com.docdost.user_service.DTO.requests.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    String login(LogInRequest request);
}

package com.docdost.user_service.controller;

import com.docdost.user_service.dto.requests.LogInRequest;
import com.docdost.user_service.dto.requests.RegisterRequest;
import com.docdost.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogInRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

package com.docdost.user_service.controller;

import com.docdost.user_service.dto.responses.UserResponse;
import com.docdost.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/username")
    public ResponseEntity<String> updateUsername(
            @RequestParam UUID userId,
            @RequestParam String newUsername) {

        userService.updateUsername(userId, newUsername);
        return ResponseEntity.ok("Username updated");
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(
            @RequestParam UUID  userId,
            @RequestParam String newPassword) {

        userService.updatePassword(userId, newPassword);
        return ResponseEntity.ok("Password updated");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }
}

package com.docdost.user_service.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequest {
    private String oldPassword;
    private String newPassword;
}

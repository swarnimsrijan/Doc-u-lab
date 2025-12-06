package com.docdost.user_service.dto.responses;

import com.docdost.user_service.enums.GlobalUserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateResponse {
    private UUID userId;
    private GlobalUserRoles oldRole;
    private GlobalUserRoles newRole;
    private LocalDateTime updatedAt;
}


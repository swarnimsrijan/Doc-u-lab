package com.docdost.user_service.dto.requests;

import com.docdost.user_service.enums.GlobalUserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateRequest {
    private GlobalUserRoles newRole;
}

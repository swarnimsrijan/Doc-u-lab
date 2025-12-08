package com.docdost.user_service.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAvailabilityResponse {
    private String username;
    private boolean available;
}

package com.finance_tracker.dto.responses.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private String currency;
    private String timezone;
}

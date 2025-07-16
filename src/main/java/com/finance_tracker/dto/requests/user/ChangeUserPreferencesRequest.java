package com.finance_tracker.dto.requests.user;

import com.finance_tracker._shared.custom_validation.ValidCurrency;
import com.finance_tracker._shared.custom_validation.ValidTimezone;
import lombok.Data;

@Data
public class ChangeUserPreferencesRequest {
    @ValidCurrency
    private String currency;

    @ValidTimezone
    private String timezone;
}

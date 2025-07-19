package com.finance_tracker.dto.requests.user;

import com.finance_tracker._shared.custom_validation.ValidCurrency;
import com.finance_tracker._shared.custom_validation.ValidTimezone;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class ChangeUserPreferencesRequest {
    @ValidCurrency
    private String currency;

    @ValidTimezone
    private String timezone;
}

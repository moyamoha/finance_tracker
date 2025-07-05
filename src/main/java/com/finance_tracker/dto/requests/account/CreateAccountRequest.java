package com.finance_tracker.dto.requests.account;

import com.finance_tracker.enums.AccountType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    @NotEmpty(message = "Account must have a name")
    private String name;

    @NotNull(message = "Account type cannot be null")
    private AccountType type;

    @NotNull
    @PositiveOrZero
    private BigDecimal initialBalance;
}

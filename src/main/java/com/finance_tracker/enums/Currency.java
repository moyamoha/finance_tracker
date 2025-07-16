package com.finance_tracker.enums;

import lombok.Getter;

public enum Currency {
    EUR("Euro", "€"),
    USD("US Dollar", "$");

    @Getter
    private final String displayName;

    @Getter
    private final String symbol;

    Currency(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }
}

package com.finance_tracker.events.events.account;

import com.finance_tracker.entity.Account;
import lombok.Getter;

public record AccountDeletedEvent(Account account) {
}

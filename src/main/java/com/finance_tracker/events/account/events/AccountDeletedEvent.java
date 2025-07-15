package com.finance_tracker.events.account.events;

import com.finance_tracker.entity.Account;

public record AccountDeletedEvent(Account account) {
}

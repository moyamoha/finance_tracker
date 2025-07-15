package com.finance_tracker.events.transaction.events;

import com.finance_tracker.entity.Transaction;

public record TransactionUpdatedEvent(
        Transaction updated,
        Transaction original
) {
}

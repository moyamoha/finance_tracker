package com.finance_tracker.events.events.transaction;

import com.finance_tracker.entity.Transaction;

public record TransactionDeletedEvent(Transaction transaction) {
}

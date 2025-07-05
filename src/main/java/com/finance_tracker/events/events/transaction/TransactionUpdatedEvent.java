package com.finance_tracker.events.events.transaction;

import com.finance_tracker.entity.Transaction;
import com.finance_tracker.events.payload.TransactionUpdateInfo;

public record TransactionUpdatedEvent(Transaction transaction, TransactionUpdateInfo oldInfo) {
}

package com.finance_tracker.dto.responses.recurring_transaction;

import lombok.Data;

import java.util.List;

@Data
public class RecurringTransactionCollectionResponse {
    private List<RecurringTransactionResponse> content;
}

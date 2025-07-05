package com.finance_tracker.dto.responses.transaction;

import com.finance_tracker.enums.TransactionCategory;

public record SingleCategoryResponse(String value, String displayName, TransactionTypeResponse transactionType) {
    public static SingleCategoryResponse fromTransactionCategory(TransactionCategory category) {
        return new SingleCategoryResponse(
                category.name(),
                category.getDisplayName(),
                new TransactionTypeResponse(category.getTransactionType().name(), category.getTransactionType().getDisplayName())
        );
    }
}

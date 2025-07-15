package com.finance_tracker.events.budget.events;

import com.finance_tracker.entity.Budget;

public record BudgetUpdatedEvent(Budget updated, Budget original) {
}

package com.finance_tracker.dto.responses.report;

import com.finance_tracker.dto.responses.account.SingleAccountResponse;
import com.finance_tracker.dto.responses.budget.BudgetResponse;
import com.finance_tracker.dto.responses.transaction.TransactionResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class SummaryResponse {
    private BigDecimal totalBalance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal savingRate;
    private List<SingleAccountResponse> accounts;
    private List<BudgetResponse> budgets;
    private List<TransactionResponse> recentTransactions;
    private List<MonthlyTrendItem> monthlyTrends;
    private List<ExpenseByCategoryItem> expensesByCategory;
}

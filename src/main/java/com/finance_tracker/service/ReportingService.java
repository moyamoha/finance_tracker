package com.finance_tracker.service;

import com.finance_tracker.dto.responses.account.SingleAccountResponse;
import com.finance_tracker.dto.responses.budget.BudgetResponse;
import com.finance_tracker.dto.responses.report.ExpenseByCategoryItem;
import com.finance_tracker.dto.responses.report.MonthlyTrendItem;
import com.finance_tracker.dto.responses.report.SummaryResponse;
import com.finance_tracker.dto.responses.transaction.TransactionResponse;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final AccountService accountService;
    private final BudgetService budgetService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public SummaryResponse getSummary(User user) {
        List<TransactionResponse> recentTransactions = transactionService.getRecentTransactions(user, 5);
        List<BudgetResponse> budgets = budgetService.getBudgetsForReport(user, 3);
        List<SingleAccountResponse> accounts = accountService.getAccountsForReport(user, 3);
        BigDecimal totalBalance = accountService.getTotalBalance(user);
        BigDecimal totalExpenses = transactionService.getTotalExpensesForCurrentMonthForUser(user);
        BigDecimal totalIncome = transactionService.getTotalIncomeForCurrentMonthForUser(user);

        List<ExpenseByCategoryItem> expensesByCategory = transactionService.expensesByCategory(user);
        List<MonthlyTrendItem> monthlyTrends = getMonthlyTrendsForThePastSixMonths(user);
        return SummaryResponse.builder()
                .totalBalance(totalBalance)
                .monthlyExpenses(totalExpenses)
                .monthlyIncome(totalIncome)
                .budgets(budgets)
                .savingRate(calculateSavingRate(totalIncome, totalExpenses))
                .accounts(accounts)
                .recentTransactions(recentTransactions)
                .expensesByCategory(expensesByCategory)
                .monthlyTrends(monthlyTrends)
                .build();
    }

    private List<MonthlyTrendItem> getMonthlyTrendsForThePastSixMonths(User user) {
        List<MonthlyTrendItem> monthlyTrends = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);

        for (int i = 5; i >= 0; i--) {
            LocalDate date = firstDayOfCurrentMonth.minusMonths(i);
            monthlyTrends.add(getMonthlyTrendForGivenMonth(user, date));
        }
        return monthlyTrends;
    }

    private MonthlyTrendItem getMonthlyTrendForGivenMonth(User user, LocalDate date) {
        BigDecimal totalIncome = transactionRepository
                .totalByTransactionTypeForGivenMonth(user, TransactionType.INCOME, date);
        BigDecimal totalExpense = transactionRepository
                .totalByTransactionTypeForGivenMonth(user, TransactionType.EXPENSE, date);
        BigDecimal savings = totalIncome.subtract(totalExpense).setScale(2, RoundingMode.HALF_UP);
        String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        return MonthlyTrendItem
                .builder()
                .month(month)
                .expenses(totalExpense)
                .income(totalIncome)
                .savings(savings)
                .build();
    }

    private BigDecimal calculateSavingRate(BigDecimal income, BigDecimal expenses) {
        BigDecimal savings = income.subtract(expenses);

        BigDecimal savingRate;

        if (income.compareTo(BigDecimal.ZERO) == 0) {
            savingRate = BigDecimal.ZERO;
        } else {
            savingRate = savings.divide(income, 2, RoundingMode.HALF_UP);
        }

        return savings.divide(income, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

}

package com.finance_tracker.schedules;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.helpers.BudgetHelper;
import com.finance_tracker.repository.budget.BudgetRepository;
import com.finance_tracker.service.BudgetService;
import com.finance_tracker.service.mailing.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonitorBudgets {

    private final BudgetRepository budgetRepository;
    private final BudgetService budgetService;
    private final MailService mailService;

    @Scheduled(cron = "0 0 4 * * *")
    public void scanBudgetsAndSendAlerts() {
        List<Budget> allBudgets = budgetRepository.findByIsActive(true);
        allBudgets.forEach((budget -> {
            if (BudgetHelper.shouldBudgetBeReset(budget)) {
                budget.setRemaining(budget.getAmount());
                budgetRepository.save(budget);
                return;
            }
            if (BudgetHelper.budgetThresholdReached(budget)) {
                try {
                    budgetService.sendAlertEmail(budget);
                    budget.setAlertSent(true);
                    budgetRepository.save(budget);
                } catch (MessagingException e) {
                    System.out.println(e.getMessage());
                }
            }
        }));
    }

}

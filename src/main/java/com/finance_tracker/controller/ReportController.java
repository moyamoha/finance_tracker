package com.finance_tracker.controller;

import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.dto.responses.budget.BudgetResponse;
import com.finance_tracker.dto.responses.report.ExpenseByCategoryItem;
import com.finance_tracker.dto.responses.report.MonthlyTrendItem;
import com.finance_tracker.dto.responses.report.SummaryResponse;
import com.finance_tracker.dto.responses.transaction.TransactionResponse;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.service.AccountService;
import com.finance_tracker.service.BudgetService;
import com.finance_tracker.service.ReportingService;
import com.finance_tracker.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@Tag(name = "Report")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReportController {
    private final ReportingService reportingService;

    @GetMapping("/summary")
    public SummaryResponse getSummary(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return reportingService.getSummary(user);
    }
}

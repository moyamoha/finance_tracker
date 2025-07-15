package com.finance_tracker.controller;

import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.dto.filter.BudgetFilterRequest;
import com.finance_tracker.dto.requests.budget.CreateBudgetRequest;
import com.finance_tracker.dto.requests.budget.EditBudgetRequest;
import com.finance_tracker.dto.responses.CollectionResponse;
import com.finance_tracker.dto.responses.budget.BudgetResponse;
import com.finance_tracker.service.BudgetService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/budget")
@Tag(name = "Budgets")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/")
    public BudgetResponse createBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CreateBudgetRequest dto
    ) {
        return budgetService.createBudget(userDetails.getUser(), dto);
    }
    
    @GetMapping("/")
    public CollectionResponse<BudgetResponse> fetchBudgets(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute BudgetFilterRequest filter,
            Pageable pageable
    ) {
        return budgetService.getListOfBudgets(userDetails.getUser(), filter, pageable);
    }

    @GetMapping("/{id}")
    public BudgetResponse getBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id
    ) {
        return budgetService.getBudget(userDetails.getUser(), id);
    }

    @PutMapping("/{id}")
    public BudgetResponse update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id,
            @RequestBody @Valid EditBudgetRequest dto
    ) {
        return budgetService.updateOne(userDetails.getUser(), id, dto);
    }
}

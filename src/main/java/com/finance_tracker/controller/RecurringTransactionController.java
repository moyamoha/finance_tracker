package com.finance_tracker.controller;

import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.dto.requests.recurring_transaction.CreateRecurringTransactionRequest;
import com.finance_tracker.dto.requests.recurring_transaction.EditRecurringTransactionRequest;
import com.finance_tracker.dto.responses.recurring_transaction.RecurringTransactionCollectionResponse;
import com.finance_tracker.dto.responses.recurring_transaction.RecurringTransactionResponse;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.service.RecurringTransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recurring-transactions")
@Tag(name = "Recurring Transactions")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class RecurringTransactionController {
    private final RecurringTransactionService recurringTransactionService;

    @PostMapping("/")
    @Auditable(actionType = "CREATE_RECURRING_TRANSACTION", resourceType = AuditResourceType.RECURRING_TRANSACTION)
    public RecurringTransactionResponse create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CreateRecurringTransactionRequest dto
    ) {
        return recurringTransactionService.createRecurringTransaction(userDetails.getUser(), dto);
    }

    @GetMapping("/")
    public RecurringTransactionCollectionResponse getAllForUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return recurringTransactionService.getAll(userDetails.getUser());
    }

    @GetMapping("/{id}")
    public RecurringTransactionResponse getOne(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id
    ) {
        return recurringTransactionService.getOne(userDetails.getUser(), id);
    }

    @PutMapping("/{id}")
    @Auditable(actionType = "UPDATE_RECURRING_TRANSACTION", resourceType = AuditResourceType.RECURRING_TRANSACTION)
    public RecurringTransactionResponse update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id,
            @RequestBody @Valid EditRecurringTransactionRequest dto
    ) {
        return recurringTransactionService.updateOne(userDetails.getUser(), id, dto);
    }

    @DeleteMapping("/{id}")
    @Auditable(actionType = "DELETE_RECURRING_TRANSACTION", resourceType = AuditResourceType.RECURRING_TRANSACTION)
    public void delete(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable UUID id) {
        recurringTransactionService.deleteOne(userDetails.getUser(), id);
    }
}

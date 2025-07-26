package com.finance_tracker.controller;

import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.dto.requests.transaction.CreateTransactionRequest;
import com.finance_tracker.dto.filter.TransactionFilterRequest;
import com.finance_tracker.dto.requests.transaction.EditTransactionRequest;
import com.finance_tracker.dto.responses.CollectionResponse;
import com.finance_tracker.dto.responses.transaction.TransactionResponse;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public CollectionResponse<TransactionResponse> getTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute TransactionFilterRequest filter,
            Pageable pageable
    ) {
        return transactionService.getTransactions(userDetails.getUser(), filter, pageable);
    }

    @PostMapping("/")
    @Auditable(actionType = "CREATE_TRANSACTION", resourceType = AuditResourceType.TRANSACTION)
    public TransactionResponse createTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody(required = true) @Valid CreateTransactionRequest dto
    ) {
        return transactionService.createTransaction(userDetails.getUser(), dto);
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id
    ) {
        return transactionService.getSingleTransaction(userDetails.getUser(), id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Auditable(actionType = "DELETE_TRANSACTION", resourceType = AuditResourceType.TRANSACTION)
    public void deleteTransaction(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable UUID id) {
        transactionService.deleteTransaction(userDetails.getUser(), id);
    }

    @PutMapping("/{id}")
    @Auditable(actionType = "UPDATE_TRANSACTION", resourceType = AuditResourceType.TRANSACTION)
    public TransactionResponse updateTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id,
            @RequestBody(required = true) @Valid EditTransactionRequest dto
    ) {
        return transactionService.updateTransaction(userDetails.getUser(), id, dto);
    }
}

package com.finance_tracker.controller;

import com.finance_tracker._shared.Identifier;
import com.finance_tracker.dto.requests.CreateTransactionRequest;
import com.finance_tracker.dto.filter.TransactionFilterRequest;
import com.finance_tracker.dto.requests.EditTransactionRequest;
import com.finance_tracker.dto.responses.SingleTransactionResponse;
import com.finance_tracker.dto.responses.TransactionCollectionResponse;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.exception.http.HttpException;
import com.finance_tracker.exception.http.ItemNotFoundException;
import com.finance_tracker.mapper.TransactionMapper;
import com.finance_tracker.repository.TransactionRepository;
import com.finance_tracker.repository.TransactionSpecification;
import com.finance_tracker.service.TransactionService;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public TransactionCollectionResponse getTransactions(
            @AuthenticationPrincipal User user,
            @ModelAttribute TransactionFilterRequest filter,
            Pageable pageable
    ) {
        return transactionService.getTransactions(user, filter, pageable);
    }

    @PostMapping("/")
    public SingleTransactionResponse createTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody(required = true) @Valid CreateTransactionRequest dto
    ) {
        return transactionService.createTransaction(user, dto);
    }

    @GetMapping("/{id}")
    public SingleTransactionResponse getTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        return transactionService.getSingleTransaction(user, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTransaction(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        transactionService.deleteTransaction(user, id);
    }

    @PutMapping("/{id}")
    public SingleTransactionResponse updateTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @RequestBody(required = true) @Valid EditTransactionRequest dto
    ) {
        return transactionService.updateTransaction(user, id, dto);
    }
}

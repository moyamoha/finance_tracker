package com.finance_tracker.controller;

import com.finance_tracker.dto.requests.CreateTransactionRequest;
import com.finance_tracker.dto.filter.TransactionFilterRequest;
import com.finance_tracker.dto.responses.SingleTransactionResponse;
import com.finance_tracker.dto.responses.TransactionCollectionResponse;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.mapper.TransactionMapper;
import com.finance_tracker.repository.TransactionRepository;
import com.finance_tracker.repository.TransactionSpecification;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/")
    public TransactionCollectionResponse getTransactions(
            @AuthenticationPrincipal User user,
            @ModelAttribute TransactionFilterRequest filter,
            Pageable pageable
    ) {
        Specification<Transaction> spec = Specification.allOf(
                TransactionSpecification.hasUserId(user.getId()),
                filter.getType() != null ? TransactionSpecification.hasType(filter.getType()) : null,
                filter.getMinAmount() != null ? TransactionSpecification.amountGreaterThanOrEqual(filter.getMinAmount()) : null,
                filter.getMaxAmount() != null ? TransactionSpecification.amountLessThanOrEqual(filter.getMaxAmount()) : null,
                filter.getStartDate() != null ? TransactionSpecification.dateAfter(filter.getStartDate()) : null,
                filter.getEndDate() != null ? TransactionSpecification.dateBefore(filter.getEndDate()) : null
        );
        return TransactionMapper.toCollectionResponse(transactionRepository.findAll(spec, pageable));
    }

    @PostMapping("/")
    public SingleTransactionResponse createTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody(required = true) @Valid CreateTransactionRequest dto
    ) {
        Transaction transaction = TransactionMapper.toEntity(user, dto);
        return TransactionMapper.toResponse(transactionRepository.save(transaction));
    }
}

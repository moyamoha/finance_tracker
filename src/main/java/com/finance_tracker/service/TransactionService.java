package com.finance_tracker.service;

import com.finance_tracker._shared.Identifier;
import com.finance_tracker.dto.filter.TransactionFilterRequest;
import com.finance_tracker.dto.requests.CreateTransactionRequest;
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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction getUserTransactionOrThrow(User user, UUID id) {
        return transactionRepository.getTransactionsByUserAndId(user, id)
                .orElseThrow(() -> ItemNotFoundException.withIdentifierAndEntity(Transaction.class, new Identifier<>(id)));
    }

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

    @Transactional
    public SingleTransactionResponse createTransaction(User user, @Valid CreateTransactionRequest dto) {
        try {
            Transaction transaction = TransactionMapper.toEntity(user, dto);
            return TransactionMapper.toResponse(transactionRepository.save(transaction));
        } catch (Exception e) {
            throw new HttpException(
                    HttpStatus.BAD_REQUEST,
                    "Bad Request",
                    e.getMessage()
            );
        }
    }

    @Transactional(readOnly = true)
    public SingleTransactionResponse getSingleTransaction(User user, UUID id) {
        Transaction transaction = getUserTransactionOrThrow(user, id);
        return TransactionMapper.toResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(User user, UUID id) {
        Transaction transaction = this.getUserTransactionOrThrow(user, id);
        transactionRepository.delete(transaction);
    }

    @Transactional
    public SingleTransactionResponse updateTransaction(
            User user,
            UUID id,
            @Valid EditTransactionRequest dto
    ) {
        Transaction transaction = this.getUserTransactionOrThrow(user, id);
        try {
            transaction.updateFromDto(dto);
            return TransactionMapper.toResponse(transaction);
        } catch (Exception e) {
            throw new HttpException(
                    HttpStatus.BAD_REQUEST,
                    "Bad request",
                    e.getMessage()
            );
        }
    }
}

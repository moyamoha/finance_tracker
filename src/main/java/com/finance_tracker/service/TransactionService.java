package com.finance_tracker.service;

import com.finance_tracker._shared.Identifier;
import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.dto.filter.TransactionFilterRequest;
import com.finance_tracker.dto.requests.transaction.CreateTransactionRequest;
import com.finance_tracker.dto.requests.transaction.EditTransactionRequest;
import com.finance_tracker.dto.responses.CollectionResponse;
import com.finance_tracker.dto.responses.report.ExpenseByCategoryItem;
import com.finance_tracker.dto.responses.transaction.TransactionResponse;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.RecurringTransaction;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.enums.TransactionCategory;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.events.transaction.events.TransactionCreatedEvent;
import com.finance_tracker.events.transaction.events.TransactionDeletedEvent;
import com.finance_tracker.events.transaction.events.TransactionUpdatedEvent;
import com.finance_tracker.exception.http.ItemNotFoundException;
import com.finance_tracker.mapper.TransactionMapper;
import com.finance_tracker.repository.transaction.TransactionRepository;
import com.finance_tracker.repository.transaction.TransactionSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BudgetService budgetService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Transaction getUserTransactionOrThrow(User user, UUID id) {
        return transactionRepository.getTransactionsByUserAndId(user, id)
                .orElseThrow(() -> ItemNotFoundException.withIdentifierAndEntity(Transaction.class, new Identifier<>(id)));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getRecentTransactions(User user, Integer limit) {
        Specification<Transaction> spec = Specification.allOf(
                TransactionSpecification.hasUserId(user.getId())
        );
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "date"));
        Page<Transaction> pagedResult = transactionRepository.findAll(spec, pageRequest);
        return pagedResult.getContent().stream().map(TransactionMapper::toResponse).toList();
    }

    public BigDecimal getTotalExpensesForCurrentMonthForUser(User user) {
        LocalDate today = LocalDate.now();
        return transactionRepository
                .totalByTransactionTypeForGivenMonth(user, TransactionType.EXPENSE, today);
    }

    public BigDecimal getTotalIncomeForCurrentMonthForUser(User user) {
        return transactionRepository
                .totalByTransactionTypeForGivenMonth(user, TransactionType.INCOME, LocalDate.now());
    }

    public List<ExpenseByCategoryItem> expensesByCategory(User user) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = LocalDate.of(today.getYear(), today.getMonth(), today.getDayOfMonth());
        List<Transaction> transactions = transactionRepository.findExpensesByUserAndGivenMonth(user, firstDayOfCurrentMonth);

        List<ExpenseByCategoryItem> result = new ArrayList<>();

        Map<TransactionCategory, BigDecimal> totalAmountByCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory, // Key mapper: groups by category name (String)
                        Collectors.reducing(     // Downstream collector: performs reduction on each group
                                BigDecimal.ZERO,         // Identity: starting sum for each category is 0
                                Transaction::getAmount,  // Mapper: gets the amount (BigDecimal) for each transaction
                                BigDecimal::add          // Accumulator: adds two BigDecimal amounts
                        )
                ));
        totalAmountByCategory.forEach(((category, amount) -> {
            result.add(new ExpenseByCategoryItem(category, amount.setScale(2, RoundingMode.HALF_UP)));
        }));
        return result;
    }

    @Transactional
    public CollectionResponse<TransactionResponse> getTransactions(
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
    @Auditable(actionType = "CREATE_TRANSACTION", resourceType = AuditResourceType.TRANSACTION)
    public TransactionResponse createTransaction(User user, @Valid CreateTransactionRequest dto) {
        Account account = accountService.getAccountForUserByIdOrThrow(user, dto.getAccountId());
        Transaction transaction = TransactionMapper.toEntity(user, dto);
        transaction.setAccount(account);
        TransactionResponse response = TransactionMapper.toResponse(transactionRepository.save(transaction));
        eventPublisher.publishEvent(new TransactionCreatedEvent(transaction));
        return response;
    }

    @Transactional(readOnly = true)
    public TransactionResponse getSingleTransaction(User user, UUID id) {
        Transaction transaction = getUserTransactionOrThrow(user, id);
        return TransactionMapper.toResponse(transaction);
    }

    @Transactional
    @Auditable(actionType = "DELETE_TRANSACTION", resourceType = AuditResourceType.TRANSACTION)
    public void deleteTransaction(User user, UUID id) {
        Transaction transaction = this.getUserTransactionOrThrow(user, id);
        transactionRepository.delete(transaction);
        eventPublisher.publishEvent(new TransactionDeletedEvent(transaction));
    }

    @Transactional
    @Auditable(actionType = "UPDATE_TRANSACTION", resourceType = AuditResourceType.TRANSACTION)
    public TransactionResponse updateTransaction(
            User user,
            UUID id,
            @Valid EditTransactionRequest dto
    ) {
        Transaction transaction = getUserTransactionOrThrow(user, id);
        Transaction original = TransactionMapper.cloneEntity(transaction);
        transaction.updateFromDto(dto);

        if (!dto.getAccountId().equals(transaction.getAccount().getId())) {
            Account newAccount = accountService.getAccountForUserByIdOrThrow(user, dto.getAccountId());
            transaction.setAccount(newAccount);
        }

        eventPublisher.publishEvent(
                new TransactionUpdatedEvent(transaction, original)
        );
        return TransactionMapper.toResponse(transaction);

    }

    public void createFromRecurringTransaction(RecurringTransaction recurringTransaction) {
        CreateTransactionRequest dto = new CreateTransactionRequest();
        dto.setDate(recurringTransaction.getNextGenerationDate().atStartOfDay());
        dto.setAmount(recurringTransaction.getAmount());
        dto.setDescription(recurringTransaction.getDescription());
        dto.setAccountId(recurringTransaction.getAccount().getId());
        dto.setCategory(recurringTransaction.getCategory());
        createTransaction(recurringTransaction.getUser(), dto);
    }
}
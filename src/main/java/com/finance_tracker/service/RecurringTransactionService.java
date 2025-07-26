package com.finance_tracker.service;

import com.finance_tracker._shared.Identifier;
import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.dto.requests.recurring_transaction.CreateRecurringTransactionRequest;
import com.finance_tracker.dto.requests.recurring_transaction.EditRecurringTransactionRequest;
import com.finance_tracker.dto.responses.recurring_transaction.RecurringTransactionCollectionResponse;
import com.finance_tracker.dto.responses.recurring_transaction.RecurringTransactionResponse;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.RecurringTransaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.exception.custom.recurring_transaction.InvalidDateRangeException;
import com.finance_tracker.exception.http.ItemNotFoundException;
import com.finance_tracker.helpers.RecurringTransactionHelper;
import com.finance_tracker.mapper.RecurringTransactionMapper;
import com.finance_tracker.repository.account.AccountRepository;
import com.finance_tracker.repository.recurring_transaction.RecurringTransactionRepository;
import com.finance_tracker.service.validators.RecurringTransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final AccountRepository accountRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final RecurringTransactionValidator validator;

    @Auditable(actionType = "CREATE_RECURRING_TRANSACTION", resourceType = AuditResourceType.RECURRING_TRANSACTION)
    public RecurringTransactionResponse createRecurringTransaction(
            User user,
            CreateRecurringTransactionRequest dto
    ) {
        Account account = getAccountOrThrow(user, dto.getAccountId());
        RecurringTransaction recurringTransaction = RecurringTransactionMapper.toEntity(user, dto);
        recurringTransaction.setAccount(account);

        LocalDate next = RecurringTransactionHelper.calculateNextGenerationDate(recurringTransaction);
        recurringTransaction.setNextGenerationDate(next);

        validator.validate(recurringTransaction);

        recurringTransactionRepository.save(recurringTransaction);
        return RecurringTransactionMapper.toSingleResponse(recurringTransaction);
    }

    public RecurringTransactionResponse getOne(User user, UUID id) {
        RecurringTransaction rt = getRecurringTransactionOrThrow(user, id);
        return RecurringTransactionMapper.toSingleResponse(rt);
    }

    public RecurringTransactionCollectionResponse getAll(User user) {
        List<RecurringTransaction> all = recurringTransactionRepository.findByUser(user);
        return RecurringTransactionMapper.toCollectionResponse(all);
    }

    @Transactional
    @Auditable(actionType = "UPDATE_RECURRING_TRANSACTION", resourceType = AuditResourceType.RECURRING_TRANSACTION)
    public RecurringTransactionResponse updateOne(User user, UUID id, EditRecurringTransactionRequest dto) {
        RecurringTransaction rt = getRecurringTransactionOrThrow(user, id);
        RecurringTransactionMapper.updateFromDto(rt, dto);

        validator.validate(rt);

        recurringTransactionRepository.save(rt);

        return RecurringTransactionMapper.toSingleResponse(rt);
    }

    @Auditable(actionType = "DELETE_RECURRING_TRANSACTION", resourceType = AuditResourceType.RECURRING_TRANSACTION)
    public void deleteOne(User user, UUID id) {
        recurringTransactionRepository.deleteByUserAndId(user, id);
    }

    public List<RecurringTransaction> getByNextGenerationDate(LocalDate date) {
        return recurringTransactionRepository.findByNextGenerationDate(LocalDate.now());
    }

    // Functions used internally in this service
    private Account getAccountOrThrow(User user, UUID accountId) {
        return accountRepository.findByUserAndId(user, accountId)
                .orElseThrow(() -> ItemNotFoundException.withIdentifierAndEntity(Account.class, new Identifier<UUID>(accountId)));
    }

    private RecurringTransaction getRecurringTransactionOrThrow(User user, UUID id) {
        return recurringTransactionRepository.findByUserAndId(user, id)
                .orElseThrow(() -> ItemNotFoundException.withIdentifierAndEntity(RecurringTransaction.class, new Identifier<>(id)));
    }
}

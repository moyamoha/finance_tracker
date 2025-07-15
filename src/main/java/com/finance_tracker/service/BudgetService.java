package com.finance_tracker.service;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker._shared.LocalDateTimeRange;
import com.finance_tracker._shared.Identifier;
import com.finance_tracker.dto.filter.BudgetFilterRequest;
import com.finance_tracker.dto.requests.budget.CreateBudgetRequest;
import com.finance_tracker.dto.requests.budget.EditBudgetRequest;
import com.finance_tracker.dto.responses.CollectionResponse;
import com.finance_tracker.dto.responses.budget.BudgetResponse;
import com.finance_tracker.entity.*;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.events.budget.events.BudgetUpdatedEvent;
import com.finance_tracker.exception.custom.budget.DuplicateBudgetException;
import com.finance_tracker.exception.custom.budget.InvalidCategoryForBudgetException;
import com.finance_tracker.exception.custom.recurring_transaction.InvalidDateRangeException;
import com.finance_tracker.exception.http.ItemNotFoundException;
import com.finance_tracker.exception.http.UnprocessableEntityException;
import com.finance_tracker.helpers.BudgetHelper;
import com.finance_tracker.mapper.BudgetMapper;
import com.finance_tracker.repository.budget.BudgetRepository;
import com.finance_tracker.repository.budget.BudgetSpecification;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final AccountService accountService;
    private final ApplicationEventPublisher eventPublisher;

    public BudgetService(BudgetRepository budgetRepository, AccountService accountService, ApplicationEventPublisher eventPublisher) {
        this.budgetRepository = budgetRepository;
        this.accountService = accountService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public BudgetResponse createBudget(User user, CreateBudgetRequest dto) {
        Account account = null;
        if (dto.getAccountId() != null) {
            account = accountService.getAccountForUserByIdOrThrow(user, dto.getAccountId());
        }
        if (budgetRepository.existsByUserAndAccountAndCategory(
                user,
                account,
                dto.getCategory()
        )) {
            throw new DuplicateBudgetException();
        }
        Budget budget = BudgetMapper.toEntity(user, dto);
        budget.setAccount(account);

        BudgetHelper.validateBudgetCategoryOrThrow(budget);
        BudgetHelper.validateBudgetDateRange(budget);

        budgetRepository.save(budget);
        return BudgetMapper.toSingleResponse(budget);
    }

    @Transactional
    public BudgetResponse getBudget(User user, UUID id) {
        Budget budget = getBudgetByUserAndIdOrThrow(user, id);
        return BudgetMapper.toSingleResponse(budget);
    }

    @Transactional
    public CollectionResponse<BudgetResponse> getListOfBudgets(User user, BudgetFilterRequest filter, Pageable pageable) {
        LocalDateTimeRange range = null;
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            range = new LocalDateTimeRange(filter.getStartDate(), filter.getEndDate());
        }
        Specification<Budget> specs = Specification.allOf(
            BudgetSpecification.hasUserId(user.getId()),
                filter.getAccountId() != null ? BudgetSpecification.hasAccountId(filter.getAccountId()) : null,
                filter.getCategory() != null ? BudgetSpecification.hasCategory(filter.getCategory().name()) : null,
                range != null ? BudgetSpecification.isInRange(range) : null,
                filter.getName() != null ? BudgetSpecification.nameContainsIgnoreCase(filter.getName()) : null
        );
        return BudgetMapper.toCollectionResponse(budgetRepository.findAll(specs, pageable));
    }

    public Budget getBudgetByUserAndIdOrThrow(User user, UUID id) {
        return budgetRepository.findByUserAndId(user, id).orElseThrow(
                () -> ItemNotFoundException.withIdentifierAndEntity(Budget.class, new Identifier<>(id))
        );
    }

    public BudgetResponse updateOne(User user, UUID id, EditBudgetRequest dto) {
        Budget budget = getBudgetByUserAndIdOrThrow(user, id);
        Budget original = BudgetMapper.cloneEntity(budget);
        BudgetMapper.updateFromDto(budget, dto);

        if (dto.getAccountId() != null) {
            Account account = accountService.getAccountForUserByIdOrThrow(user, dto.getAccountId());
            budget.setAccount(account);
        }

        BudgetHelper.validateBudgetCategoryOrThrow(budget);
        BudgetHelper.validateBudgetDateRange(budget);

        budgetRepository.save(budget);
        eventPublisher.publishEvent(new BudgetUpdatedEvent(budget, original));
        return BudgetMapper.toSingleResponse(budget);
    }

    public List<Budget> findForTransaction(Transaction transaction) {
        return budgetRepository.findForTransaction(transaction);
    }
}
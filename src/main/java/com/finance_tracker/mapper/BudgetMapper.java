package com.finance_tracker.mapper;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker._shared.LocalDateTimeRange;
import com.finance_tracker.dto.requests.budget.CreateBudgetRequest;
import com.finance_tracker.dto.requests.budget.EditBudgetRequest;
import com.finance_tracker.dto.responses.CollectionResponse;
import com.finance_tracker.dto.responses.budget.BudgetResponse;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.User;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public class BudgetMapper {

    public static Budget toEntity(User user, CreateBudgetRequest dto) {
        LocalDateRange range = new LocalDateRange(dto.getStartDate(), dto.getEndDate());
        Budget budget = new Budget(user, dto.getAmount(), range);
        budget.setName(dto.getName());
        budget.setCategory(dto.getCategory());
        budget.setPeriod(dto.getPeriod());
        budget.setUser(user);
        return budget;
    }

    public static BudgetResponse toSingleResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getUser().getId(),
                budget.getAccount() != null ? budget.getAccount().getId() : null,
                budget.getCategory(),
                budget.getPeriod(),
                budget.getName(),
                budget.getAmount(),
                budget.getRemaining(),
                budget.getIsActive(),
                budget.getStartDate(),
                budget.getEndDate(),
                budget.getCreatedAt(),
                budget.getUpdatedAt()
        );
    }

    public static CollectionResponse<BudgetResponse> toCollectionResponse(Page<Budget> all) {
        List<BudgetResponse> content = all.stream().map(BudgetMapper::toSingleResponse).toList();
        return CollectionResponse.<BudgetResponse>builder()
                .content(content)
                .totalElements(all.getTotalElements())
                .page(all.getNumber() + 1)
                .size(all.getSize())
                .isLast(all.isLast())
                .build();
    }

    public static void updateFromDto(Budget budget, EditBudgetRequest dto) {
        if (dto.getName() != null) budget.setName(dto.getName());
        if (dto.getAmount() != null) {
            BigDecimal diff = budget.getRemaining().subtract(dto.getAmount());
            budget.setAmount(dto.getAmount());
            budget.setRemaining(budget.getRemaining().add(diff));
        }
        if (dto.getCategory() != null) budget.setCategory(dto.getCategory());
        if (dto.getStartDate() != null) budget.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) budget.setEndDate(dto.getEndDate());
    }

    public static Budget cloneEntity(Budget budget) {
        Budget cloned = new Budget();
        cloned.setAccount(budget.getAccount());
        cloned.setUser(budget.getUser());
        cloned.setCategory(budget.getCategory());
        cloned.setName(budget.getName());
        cloned.setEndDate(budget.getEndDate());
        cloned.setStartDate(budget.getStartDate());
        cloned.setRemaining(budget.getRemaining());
        cloned.setIsActive(budget.getIsActive());
        cloned.setId(budget.getId());
        return cloned;
    }
}

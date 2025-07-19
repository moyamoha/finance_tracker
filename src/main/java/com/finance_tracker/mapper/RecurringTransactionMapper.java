package com.finance_tracker.mapper;

import com.finance_tracker.dto.requests.recurring_transaction.CreateRecurringTransactionRequest;
import com.finance_tracker.dto.requests.recurring_transaction.EditRecurringTransactionRequest;
import com.finance_tracker.dto.responses.CollectionResponse;
import com.finance_tracker.dto.responses.recurring_transaction.RecurringTransactionCollectionResponse;
import com.finance_tracker.dto.responses.recurring_transaction.RecurringTransactionResponse;
import com.finance_tracker.entity.RecurringTransaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.helpers.RecurringTransactionHelper;

import java.time.LocalDate;
import java.util.List;

public class RecurringTransactionMapper {

    public static RecurringTransaction toEntity(User user, CreateRecurringTransactionRequest dto) {
        return RecurringTransaction.builder()
                .user(user)
                .amount(dto.getAmount())
                .category(dto.getCategory())
                .frequency(dto.getFrequency())
                .type(dto.getCategory().getTransactionType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .lastGeneratedDate(null)
                .build();
    }

    public static RecurringTransactionResponse toSingleResponse(RecurringTransaction recurringTransaction) {
        return RecurringTransactionResponse.builder()
                .id(recurringTransaction.getId())
                .userId(recurringTransaction.getUser().getId())
                .accountId(recurringTransaction.getAccount().getId())
                .category(recurringTransaction.getCategory())
                .frequency(recurringTransaction.getFrequency())
                .description(recurringTransaction.getDescription())
                .amount(recurringTransaction.getAmount())
                .startDate(recurringTransaction.getStartDate())
                .endDate(recurringTransaction.getEndDate())
                .lastGeneratedDate(recurringTransaction.getLastGeneratedDate())
                .nextGenerationDate(recurringTransaction.getNextGenerationDate())
                .createdAt(recurringTransaction.getCreatedAt())
                .updatedAt(recurringTransaction.getUpdatedAt())
                .build();
    }

    public static RecurringTransactionCollectionResponse toCollectionResponse(List<RecurringTransaction> all) {
        RecurringTransactionCollectionResponse response = new RecurringTransactionCollectionResponse();
        response.setContent(
                all.stream().map(RecurringTransactionMapper::toSingleResponse).toList()
        );
        return response;
    }

    public static void updateFromDto(RecurringTransaction rt, EditRecurringTransactionRequest dto) {
        if (dto.getAmount() != null) rt.setAmount(dto.getAmount());
        if (dto.getStartDate() != null) rt.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null ) rt.setStartDate(dto.getEndDate());
        if (dto.getDescription() != null) rt.setDescription(dto.getDescription());
        if (dto.getCategory() != null) {
            rt.setCategory(dto.getCategory());
            rt.setType(dto.getCategory().getTransactionType());
        }
        if (dto.getFrequency() != null) {
            rt.setFrequency(dto.getFrequency());
            LocalDate next = RecurringTransactionHelper.calculateNextGenerationDate(rt);
            rt.setNextGenerationDate(next);
        }
    }
}

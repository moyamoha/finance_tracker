package com.finance_tracker.mapper;

import com.finance_tracker.dto.requests.transaction.CreateTransactionRequest;
import com.finance_tracker.dto.responses.CollectionResponse;
import com.finance_tracker.dto.responses.transaction.TransactionResponse;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import org.springframework.data.domain.Page;
import java.util.List;

public class TransactionMapper {

    public static Transaction toEntity(User user, CreateTransactionRequest dto) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setCategory(dto.getCategory());
        transaction.setType(dto.getCategory().getTransactionType());
        transaction.setDescription(dto.getDescription());
        return transaction;
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setUserId(transaction.getUser().getId());
        response.setAccountId(transaction.getAccount().getId());
        response.setAmount(transaction.getAmount());
        response.setCategory(transaction.getCategory());
        response.setDescription(transaction.getDescription());
        response.setDate(transaction.getDate());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }

    public static CollectionResponse<TransactionResponse> toCollectionResponse(Page<Transaction> pagedResponse) {
        List<TransactionResponse> content = pagedResponse.stream().map(TransactionMapper::toResponse).toList();
        return CollectionResponse.<TransactionResponse>builder()
                .content(content)
                .totalElements(pagedResponse.getTotalElements())
                .page(pagedResponse.getNumber() + 1)
                .size(pagedResponse.getSize())
                .isLast(pagedResponse.isLast())
                .build();
    }

    public static Transaction cloneEntity(Transaction original) {
        if (original == null) {
            return null;
        }
        Transaction cloned = new Transaction(); // Requires a public no-arg constructor if not using Lombok's @Builder(toBuilder = true)

        cloned.setUser(original.getUser());
        cloned.setAccount(original.getAccount());
        cloned.setAmount(original.getAmount());
        cloned.setType(original.getType());
        cloned.setCategory(original.getCategory());
        cloned.setDescription(original.getDescription());
        cloned.setDate(original.getDate());

        return cloned;
    }
}

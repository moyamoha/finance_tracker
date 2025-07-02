package com.finance_tracker.mapper;

import com.finance_tracker.dto.requests.CreateTransactionRequest;
import com.finance_tracker.dto.responses.SingleTransactionResponse;
import com.finance_tracker.dto.responses.TransactionCollectionResponse;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class TransactionMapper {

    public static Transaction toEntity(User user, CreateTransactionRequest dto) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setDescription(dto.getDescription());
        return transaction;
    }

    public static SingleTransactionResponse toResponse(Transaction transaction) {
        SingleTransactionResponse response = new SingleTransactionResponse();
        response.setId(transaction.getId());
        response.setUserId(transaction.getUser().getId());
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setCategory(transaction.getCategory());
        response.setDescription(transaction.getDescription());
        response.setDate(transaction.getDate());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }

    public static TransactionCollectionResponse toCollectionResponse(Page<Transaction> pagedResponse) {
        List<SingleTransactionResponse> content = pagedResponse.stream().map(TransactionMapper::toResponse).toList();
        return new TransactionCollectionResponse(
                content,
                pagedResponse.getNumberOfElements(),
                pagedResponse.getNumber() + 1,
                pagedResponse.getSize(),
                pagedResponse.isLast()
        );
    }
}

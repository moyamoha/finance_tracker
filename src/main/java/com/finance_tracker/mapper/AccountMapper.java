package com.finance_tracker.mapper;

import com.finance_tracker.dto.requests.account.CreateAccountRequest;
import com.finance_tracker.dto.responses.account.AccountCollectionResponse;
import com.finance_tracker.dto.responses.account.SingleAccountResponse;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.User;

import java.util.List;

public class AccountMapper {

    public static Account toEntity(User user, CreateAccountRequest dto) {
        Account account = new Account(user, dto.getInitialBalance());
        account.setName(dto.getName());
        account.setType(dto.getType());
        return account;
    }

    public static SingleAccountResponse toSingleResponse(Account account) {
        SingleAccountResponse response = new SingleAccountResponse();
        response.setId(account.getId());
        response.setName(account.getName());
        response.setType(account.getType());
        response.setUserId(account.getUser().getId());
        response.setBalance(account.getBalance());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }

    public static AccountCollectionResponse toCollectionResponse(List<Account> accountsList) {
        List<SingleAccountResponse> content = accountsList.stream().map(AccountMapper::toSingleResponse).toList();
        Long totalElements = accountsList.stream().count();
        return new AccountCollectionResponse(content, totalElements);
    }
}

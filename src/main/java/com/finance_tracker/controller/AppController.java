package com.finance_tracker.controller;

import com.finance_tracker.dto.responses.account.AccountTypeResponse;
import com.finance_tracker.dto.responses.transaction.SingleCategoryResponse;
import com.finance_tracker.enums.AccountType;
import com.finance_tracker.enums.TransactionCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/categories")
    public List<SingleCategoryResponse> fetchCategories() {
        return Arrays.stream(TransactionCategory.values()).map(SingleCategoryResponse::fromTransactionCategory).toList();
    }

    @GetMapping("/account-types")
    public List<AccountTypeResponse> fetchAccountTypes() {
        return Arrays.stream(AccountType.values()).map(AccountTypeResponse::fromAccountType).toList();
    }
}

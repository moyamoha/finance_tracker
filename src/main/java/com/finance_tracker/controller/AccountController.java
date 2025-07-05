package com.finance_tracker.controller;

import com.finance_tracker.dto.filter.AccountFilterRequest;
import com.finance_tracker.dto.requests.account.CreateAccountRequest;
import com.finance_tracker.dto.requests.account.EditAccountRequest;
import com.finance_tracker.dto.responses.account.AccountCollectionResponse;
import com.finance_tracker.dto.responses.account.SingleAccountResponse;
import com.finance_tracker.entity.User;
import com.finance_tracker.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/")
    public SingleAccountResponse createAccount(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateAccountRequest dto
            ) {
        return accountService.createAccount(user, dto);
    }

    @GetMapping("/")
    public AccountCollectionResponse getAccountsForUser(
            @AuthenticationPrincipal User user,
            @ModelAttribute AccountFilterRequest filter
            ) {
        return accountService.fetchAccountsOfUser(user, filter);
    }

    @GetMapping("/{id}")
    public SingleAccountResponse getAccount(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        return accountService.getAccount(user, id);
    }

    @PutMapping("/{id}")
    public SingleAccountResponse updateAccount(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @RequestBody @Valid EditAccountRequest dto
    ) {
        return accountService.updateAccount(user, id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        this.accountService.deleteAccount(user, id);
    }
}

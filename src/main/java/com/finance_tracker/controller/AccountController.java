package com.finance_tracker.controller;

import com.finance_tracker.authentication.CustomUserDetails;
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
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CreateAccountRequest dto
    ) {
        User user = userDetails.getUser();
        return accountService.createAccount(user, dto);
    }

    @GetMapping("/")
    public AccountCollectionResponse getAccountsForUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute AccountFilterRequest filter
    ) {
        return accountService.fetchAccountsOfUser(userDetails.getUser(), filter);
    }

    @GetMapping("/{id}")
    public SingleAccountResponse getAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id
    ) {
        return accountService.getAccount(userDetails.getUser(), id);
    }

    @PutMapping("/{id}")
    public SingleAccountResponse updateAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id,
            @RequestBody @Valid EditAccountRequest dto
    ) {
        return accountService.updateAccount(userDetails.getUser(), id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID id
    ) {
        this.accountService.deleteAccount(userDetails.getUser(), id);
    }
}

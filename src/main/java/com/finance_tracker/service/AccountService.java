package com.finance_tracker.service;

import com.finance_tracker._shared.Identifier;
import com.finance_tracker.dto.filter.AccountFilterRequest;
import com.finance_tracker.dto.requests.account.CreateAccountRequest;
import com.finance_tracker.dto.requests.account.EditAccountRequest;
import com.finance_tracker.dto.responses.account.AccountCollectionResponse;
import com.finance_tracker.dto.responses.account.SingleAccountResponse;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.User;
import com.finance_tracker.events.events.account.AccountDeletedEvent;
import com.finance_tracker.exception.http.HttpException;
import com.finance_tracker.exception.http.ItemNotFoundException;
import com.finance_tracker.mapper.AccountMapper;
import com.finance_tracker.repository.AccountRepository;
import com.finance_tracker.repository.AccountSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public SingleAccountResponse createAccount(User user, CreateAccountRequest dto) {
        try {
            Account account = AccountMapper.toEntity(user, dto);
            accountRepository.save(account);
            return AccountMapper.toSingleResponse(account);
        } catch (Exception e) {
            throw new HttpException(
                    HttpStatus.BAD_REQUEST,
                    "Bad request",
                    e.getMessage()
            );
        }
    }

    @Transactional(readOnly = true)
    public SingleAccountResponse getAccount(User user, UUID id) {
        Account foundAccount = this.getAccountForUserByIdOrThrow(user, id);
        return AccountMapper.toSingleResponse(foundAccount);
    }

    @Transactional
    public void deleteAccount(User user, UUID id) {
        Account foundAccount = this.getAccountForUserByIdOrThrow(user, id);
        accountRepository.delete(foundAccount);
        eventPublisher.publishEvent(new AccountDeletedEvent(foundAccount));
    }

    @Transactional
    public SingleAccountResponse updateAccount(
            User user,
            UUID id,
            @Valid EditAccountRequest dto
    ) {
        Account account = this.getAccountForUserByIdOrThrow(user, id);
        try {
            account.updateFromDto(dto);
            return AccountMapper.toSingleResponse(account);
        } catch (Exception e) {
            throw new HttpException(
                    HttpStatus.BAD_REQUEST,
                    "Bad request",
                    e.getMessage()
            );
        }
    }

    public Account getAccountForUserByIdOrThrow(User user, UUID id) {
        return accountRepository.findByUserAndId(user, id).orElseThrow(
                () -> ItemNotFoundException.withIdentifierAndEntity(Account.class, new Identifier<>(id))
        );
    }

    @Transactional
    public AccountCollectionResponse fetchAccountsOfUser(User user, AccountFilterRequest filter) {
        Specification<Account> specs = Specification.allOf(
                AccountSpecification.hasUserId(user.getId()),
                filter.getType() != null ? AccountSpecification.hasType(filter.getType()) : null,
                filter.getName() != null ? AccountSpecification.nameContainsIgnoreCase(filter.getName()) : null
                );
        return AccountMapper.toCollectionResponse(accountRepository.findAll(specs));
    }
}

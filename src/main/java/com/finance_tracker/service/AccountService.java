package com.finance_tracker.service;

import com.finance_tracker._shared.Identifier;
import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.dto.filter.AccountFilterRequest;
import com.finance_tracker.dto.requests.account.CreateAccountRequest;
import com.finance_tracker.dto.requests.account.EditAccountRequest;
import com.finance_tracker.dto.responses.account.AccountCollectionResponse;
import com.finance_tracker.dto.responses.account.SingleAccountResponse;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.events.account.events.AccountDeletedEvent;
import com.finance_tracker.exception.custom.account.AccountWithTheSameNameAlreadyExists;
import com.finance_tracker.exception.http.ItemNotFoundException;
import com.finance_tracker.helpers.AccountHelper;
import com.finance_tracker.mapper.AccountMapper;
import com.finance_tracker.repository.account.AccountRepository;
import com.finance_tracker.repository.account.AccountSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Auditable(actionType = "CREATE_ACCOUNT", resourceType = AuditResourceType.ACCOUNT)
    public SingleAccountResponse createAccount(User user, CreateAccountRequest dto) {
        if (accountAlreadyExistsByName(user, dto.getName())) {
            throw new AccountWithTheSameNameAlreadyExists();
        }
        Account account = AccountMapper.toEntity(user, dto);
        accountRepository.save(account);
        return AccountMapper.toSingleResponse(account);
    }

    @Transactional(readOnly = true)
    public SingleAccountResponse getAccount(User user, UUID id) {
        Account foundAccount = this.getAccountForUserByIdOrThrow(user, id);
        return AccountMapper.toSingleResponse(foundAccount);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalBalance(User user) {
        BigDecimal total =  accountRepository.totalBalanceByUser(user);
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    @Auditable(actionType = "DELETE_ACCOUNT", resourceType = AuditResourceType.ACCOUNT)
    public void deleteAccount(User user, UUID id) {
        Account foundAccount = this.getAccountForUserByIdOrThrow(user, id);
        accountRepository.delete(foundAccount);
        eventPublisher.publishEvent(new AccountDeletedEvent(foundAccount));
    }

    @Transactional
    @Auditable(actionType = "UPDATE_ACCOUNT", resourceType = AuditResourceType.ACCOUNT)
    public SingleAccountResponse updateAccount(
            User user,
            UUID id,
            @Valid EditAccountRequest dto
    ) {
        Account account = this.getAccountForUserByIdOrThrow(user, id);
        account.updateFromDto(dto);
        return AccountMapper.toSingleResponse(account);
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

    public Boolean accountAlreadyExistsByName(User user, String name) {
        return accountRepository.existsByUserAndName(user, name);
    }

    public Account getAccountForUserByIdOrThrow(User user, UUID id) {
        return accountRepository.findByUserAndId(user, id).orElseThrow(
                () -> ItemNotFoundException.withIdentifierAndEntity(Account.class, new Identifier<>(id))
        );
    }

    public List<SingleAccountResponse> getAccountsForReport(User user, int limit) {
        Specification<Account> specs = Specification.allOf(AccountSpecification.hasUserId(user.getId()));
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.Direction.DESC, "balance");
        Page<Account> pagedResult = accountRepository.findAll(specs, pageRequest);
        return pagedResult.getContent().stream().map(AccountMapper::toSingleResponse).toList();
    }

}

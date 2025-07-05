package com.finance_tracker.entity;

import com.finance_tracker.dto.requests.account.EditAccountRequest;
import com.finance_tracker.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    private String name;

    @Enumerated(EnumType.STRING)
    @Setter
    private AccountType type;

    @Column(updatable = false, nullable = false)
    @Getter
    private BigDecimal initialBalance;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    @Setter
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    protected Account() {}

    public Account(User user, BigDecimal initialBalance) {
        this.user = user;
        this.createdAt = new Date();
        this.initialBalance = initialBalance;
    }

    public void incrementBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void decrementBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void updateFromDto(EditAccountRequest dto) {
        if (dto.getName() != null) this.setName(dto.getName());
        if (dto.getType() != null) this.setType(dto.getType());
    }
}

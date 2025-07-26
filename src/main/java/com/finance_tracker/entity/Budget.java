package com.finance_tracker.entity;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker.enums.BudgetPeriod;
import com.finance_tracker.enums.TransactionCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "account_id", "category", "period" })
})
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TransactionCategory category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal remaining;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BudgetPeriod period;

    private Boolean isActive = true;
    private Boolean alertSent = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Budget(User user, BigDecimal amount, @org.jetbrains.annotations.NotNull LocalDateRange range) {
        this.user = user;
        this.amount = amount;
        this.remaining = amount;
        this.startDate = range.start();
        this.endDate = range.end();
    }

    public void decrementRemaining(BigDecimal amount) {
        this.remaining = this.remaining.subtract(amount);
    }

    public void incrementRemaining(BigDecimal amount) {
        this.remaining = this.remaining.add(amount);
    }
}

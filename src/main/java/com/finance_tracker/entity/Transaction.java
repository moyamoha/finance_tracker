package com.finance_tracker.entity;

import com.finance_tracker.dto.requests.transaction.EditTransactionRequest;
import com.finance_tracker.enums.TransactionCategory;
import com.finance_tracker.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @Setter
    private Account account;

    @Column(nullable = false, precision = 15, scale = 2)
    @Setter
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private TransactionCategory category;

    @Lob
    @Setter
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private Date date;

    @CreatedDate
    private Date createdAt;

    public void updateFromDto(EditTransactionRequest dto) {
        if (dto.getCategory() != null) { this.setCategory(dto.getCategory()); }
        if (dto.getDate() != null) { this.setDate(dto.getDate()); }
        if (dto.getAmount() != null) { this.setAmount(dto.getAmount()); }
        if (dto.getType() != null) { this.setType(dto.getType()); }
        if (dto.getDescription() != null) { this.setDescription(dto.getDescription()); }
    }
}

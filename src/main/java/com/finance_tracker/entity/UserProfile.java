package com.finance_tracker.entity;

import com.finance_tracker.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZoneId;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @Column(nullable = true, length = 3)
    @Setter
    private String currency;

    @Column(name = "timezone")
    @Setter
    private String timezone;
}

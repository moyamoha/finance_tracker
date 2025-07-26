package com.finance_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String password;

    private LocalDateTime joinedAt;
    private LocalDateTime lastLoggedIn;
    private LocalDateTime markedInactiveAt;

    @Column(nullable = false)
    private boolean mfaEnabled = false;

    @Column(nullable = false)
    private boolean emailConfirmed = false;

    private String currency;
    private String timezone;

    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
    }
}

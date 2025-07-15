package com.finance_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Setter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(name = "hashed_password", nullable = false)
    private String password;

    @Setter
    private LocalDateTime joinedAt;

    @Setter
    private LocalDateTime lastLoggedIn;

    @Setter
    private LocalDateTime markedInactiveAt;

    @Setter
    private Boolean isMfaEnabled = false;

    @Setter
    private Boolean emailConfirmed = false;

    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
    }
}

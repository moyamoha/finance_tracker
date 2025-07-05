package com.finance_tracker.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column()
    private BigDecimal amount;
}

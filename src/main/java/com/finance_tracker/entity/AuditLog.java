package com.finance_tracker.entity;

import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.enums.AuditStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String actionType;

    @Lob
    private String payload;

    @Lob
    private String result;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private AuditStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditResourceType resourceType;

    @CreatedDate
    private LocalDateTime date;
}

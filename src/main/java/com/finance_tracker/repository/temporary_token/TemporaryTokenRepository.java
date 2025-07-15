package com.finance_tracker.repository.temporary_token;

import com.finance_tracker.entity.TemporaryToken;
import com.finance_tracker.enums.TempTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemporaryTokenRepository extends JpaRepository<TemporaryToken, UUID> {
    Optional<TemporaryToken> findByTokenKeyAndValueAndTokenTypeAndExpiresAtAfter(String key, String value, TempTokenType type, LocalDateTime date);
    void deleteByTokenKey(String email);
    void deleteByExpiresAtBefore(LocalDateTime expiresAtBefore);
}

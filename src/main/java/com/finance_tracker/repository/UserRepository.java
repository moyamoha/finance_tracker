package com.finance_tracker.repository;

import com.finance_tracker.entity.User;
import org.hibernate.annotations.SQLSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByMarkedInactiveAtAndLastLoggedInBefore(LocalDateTime inactiveSince, LocalDateTime date);
    List<User> findByMarkedInactiveAtBefore(LocalDateTime date);
    void deleteByEmailConfirmedAndJoinedAtBefore(Boolean emailConfirm, LocalDateTime date);
}

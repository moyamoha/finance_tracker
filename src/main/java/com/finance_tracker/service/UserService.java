package com.finance_tracker.service;

import com.finance_tracker.dto.requests.authentication.CreateUserRequest;
import com.finance_tracker.dto.requests.user.ChangeMfaSettingsRequest;
import com.finance_tracker.dto.requests.user.ChangeUserPreferencesRequest;
import com.finance_tracker.entity.User;
import com.finance_tracker.entity.UserProfile;
import com.finance_tracker.events.user.events.UserCreatedEvent;
import com.finance_tracker.events.user.events.UserMarkedForDeletionEvent;
import com.finance_tracker.exception.custom.user.MfaAlreadyDisabledException;
import com.finance_tracker.exception.custom.user.MfaAlreadyEnabledException;
import com.finance_tracker.exception.custom.user.IncorrectPasswordForActionException;
import com.finance_tracker.exception.custom.user.UserAlreadyRegisteredException;
import com.finance_tracker.mapper.UserMapper;
import com.finance_tracker.repository.user.UserProfileRepository;
import com.finance_tracker.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {


    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private BCryptPasswordEncoder encoder;
    private ApplicationEventPublisher eventPublisher;

    public User createUser(CreateUserRequest dto) {
        throwIfAlreadyRegistered(dto.getEmail());
        String hashedPassword = encoder.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        User newUser = UserMapper.toEntity(dto);

        UserProfile userProfile = new UserProfile();
        if (dto.getTimezone() != null) userProfile.setTimezone(dto.getTimezone());
        if (dto.getCurrency() != null) userProfile.setCurrency(dto.getCurrency());

        userRepository.save(newUser);
        userProfile.setUser(newUser);
        userProfileRepository.save(userProfile);
        eventPublisher.publishEvent(new UserCreatedEvent(newUser));
        return newUser;
    }

    public void changeMfaSetting(User user, ChangeMfaSettingsRequest dto) {
        checkIfPasswordMatchesOrElseThrow(user, dto.getPassword());
        if (dto.getIsMfaEnabled() && user.getIsMfaEnabled()) {
            throw new MfaAlreadyEnabledException(user.getEmail());
        }
        if (!dto.getIsMfaEnabled() && !user.getIsMfaEnabled()) {
            throw new MfaAlreadyDisabledException(user.getEmail());
        }
        user.setIsMfaEnabled(dto.getIsMfaEnabled());
        userRepository.save(user);
    }

    public void deactivateUserAccount(User user, String password) {
        checkIfPasswordMatchesOrElseThrow(user, password);
        user.setMarkedInactiveAt(LocalDateTime.now());
        userRepository.save(user);
        eventPublisher.publishEvent(new UserMarkedForDeletionEvent(user));
    }

    public UserProfile changeUserPreferences(UserProfile userProfile, ChangeUserPreferencesRequest dto) {
        if (dto.getTimezone() != null) userProfile.setTimezone(dto.getTimezone());
        if (dto.getCurrency() != null) userProfile.setCurrency(dto.getCurrency());

        return userProfileRepository.save(userProfile);
    }

    private void throwIfAlreadyRegistered(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyRegisteredException(email);
        }
    }

    private void checkIfPasswordMatchesOrElseThrow(User user, String password) {
        boolean isPasswordCorrect = encoder.matches(password, user.getPassword());
        if (!isPasswordCorrect) {
            throw new IncorrectPasswordForActionException();
        }
    }
}

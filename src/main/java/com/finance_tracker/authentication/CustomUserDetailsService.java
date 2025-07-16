package com.finance_tracker.authentication;

import com.finance_tracker.entity.User;
import com.finance_tracker.entity.UserProfile;
import com.finance_tracker.exception.authentication.UserNotFoundException;
import com.finance_tracker.repository.user.UserProfileRepository;
import com.finance_tracker.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + email)
        );
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile prof = new UserProfile();
                    prof.setUser(user);
                    userProfileRepository.save(prof);
                    return prof;
                });
        return new CustomUserDetails(user, profile);
    }
}

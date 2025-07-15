package com.finance_tracker.authentication;

import com.finance_tracker.entity.User;
import com.finance_tracker.exception.authentication.UserNotFoundException;
import com.finance_tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + email)
        );
        return fromEntityUser(user);
    }

    private CustomUserDetails fromEntityUser(User user) {
        return new CustomUserDetails(user);
    }
}

package com.finance_tracker.authentication;

import com.finance_tracker.dto.requests.LoginRequest;
import com.finance_tracker.dto.responses.authentication.SuccessfulLoginTokenResponse;
import com.finance_tracker.exception.authentication.PasswordNotMatchedException;
import com.finance_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthenticationService {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public SuccessfulLoginTokenResponse login (LoginRequest dto) {
        UserDetails details = this.userDetailsService.loadUserByUsername(dto.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean matches = encoder.matches(dto.getPassword(), details.getPassword());
        if (!matches) {
            throw new PasswordNotMatchedException("Password is invalid/wrong");
        }

        String token = jwtService.generateToken(details.getUsername());
        return new SuccessfulLoginTokenResponse(token);
    }
}

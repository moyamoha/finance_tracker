package com.finance_tracker.authentication;

import com.finance_tracker._shared.SystemConstants;
import com.finance_tracker.entity.UserProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.finance_tracker.entity.User;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final User user;

    @Getter
    @Setter
    private UserProfile userProfile;

    public CustomUserDetails(User entityUser) {
        this.user = entityUser;
    }

    public CustomUserDetails(User user, UserProfile userProfile) {
        this.user = user;
        this.userProfile = userProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getEmail().equals(SystemConstants.ANONYMOUS_USER_EMAIL);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

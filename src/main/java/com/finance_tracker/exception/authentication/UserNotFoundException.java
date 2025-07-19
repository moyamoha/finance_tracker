package com.finance_tracker.exception.authentication;

import com.finance_tracker.entity.User;

public class UserNotFoundException extends CustomAuthenticationException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException withUser(User user) {
        String buffer = "User with email " +
                user.getEmail() +
                " was not found";
        return new UserNotFoundException(buffer);
    }
}

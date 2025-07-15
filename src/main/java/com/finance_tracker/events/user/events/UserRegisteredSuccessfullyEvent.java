package com.finance_tracker.events.user.events;

import com.finance_tracker.entity.TemporaryToken;
import com.finance_tracker.entity.User;

public record UserRegisteredSuccessfullyEvent(User user, TemporaryToken token) {
}

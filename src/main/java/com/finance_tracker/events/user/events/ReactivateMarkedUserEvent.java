package com.finance_tracker.events.user.events;

import com.finance_tracker.entity.User;

public record ReactivateMarkedUserEvent(User user) {
}

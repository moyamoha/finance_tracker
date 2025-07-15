package com.finance_tracker.service.mailing;

import java.util.Map;

public record EmailPayload(
        String receiver,
        String subject,
        Map<String, Object> variables
) {
}

package com.finance_tracker.events;

import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.entity.AuditLog;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.enums.AuditStatus;
import com.finance_tracker.exception.UserReadableHttpException;
import com.finance_tracker.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final AuditLogService auditLogService;

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        String ip = getIpAddressFromEvent(event);
        String username = principal.toString();

        if (principal instanceof CustomUserDetails) {
            username = ((CustomUserDetails) principal).getUser().getId().toString();
        }

        AuditLog auditLog = AuditLog.builder()
                .actionType("USER_LOGIN")
                .payload("Trying to login")
                .result("User logged in successfully.")
                .status(AuditStatus.SUCCESS)
                .resourceType(AuditResourceType.AUTHENTICATION)
                .userId(username)
                .build();

        auditLogService.saveAuditLog(auditLog);
        log.info("Successful login for user: {} from IP: {}", username, ip);
    }

    @EventListener
    public void handleAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        String ipAddress = getIpAddressFromEvent(event);
        String reason = event.getException().getMessage();

        AuditLog auditLog = AuditLog.builder()
                .actionType("USER_LOGIN_ATTEMPT")
                .payload("Failed login attempt due to bad credentials.")
                .result("Reason: " + reason)
                .status(AuditStatus.FAILURE)
                .resourceType(AuditResourceType.AUTHENTICATION)
                .userId(username)
                .build();

        auditLogService.saveAuditLog(auditLog);
        log.warn("Failed login attempt for user: {} from IP: {}. Reason: {}", username, ipAddress, reason);
    }

    private String getIpAddressFromEvent(ApplicationEvent event) {
        if (event.getSource() instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails details = (WebAuthenticationDetails) event.getSource();
            return details.getRemoteAddress();
        }
        return "N/A";
    }
}

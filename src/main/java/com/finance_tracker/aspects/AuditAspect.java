package com.finance_tracker.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance_tracker._shared.SystemConstants;
import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.entity.AuditLog;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.enums.AuditStatus;
import com.finance_tracker.exception.UserReadableHttpException;
import com.finance_tracker.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);
    private final AuditLogService auditService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Map<String, Object> details = new HashMap<>();
        Map<String, Object> resultsObj = new HashMap<>();

        if (auditable.includeArgs()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            Map<String, Object> argsMap = new HashMap<>();
            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals("dto") || parameterNames[i].equals("id")) {
                    if (args[i] != null && !isSensitiveType(args[i])) { // Implement isSensitiveType
                        argsMap.put(parameterNames[i], args[i]);
                    } else {
                        argsMap.put(parameterNames[i], "[SENSITIVE_DATA_OR_TOO_LARGE]");
                    }
                }
            }
            details.put("arguments", argsMap);
        }

        Object result = null;
        boolean success = false;
        Throwable caughtException = null;

        try {
            result = joinPoint.proceed();
            success = true;
            if (auditable.includeResult() && result != null && !isSensitiveType(result)) {
                resultsObj.put("result", result);
            }
            return result;
        } catch (Throwable e) {
            success = false;
            caughtException = e;
            if (e instanceof UserReadableHttpException) {
                resultsObj.put("error", ((UserReadableHttpException) e).getUserReadablePayload());
            }
        } finally {
            String detailsJson = "{}";
            String resultsJson = "{}";
            try {
                detailsJson = objectMapper.writeValueAsString(details);
                resultsJson = objectMapper.writeValueAsString(resultsObj);
            } catch (Throwable e) {
                logger.error("Failed to convert audit details to JSON: {}", e.getMessage());
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userId = "";
            if (auth == null) {
                userId = SystemConstants.AUDIT_BY_SYSTEM;
            } else {
                CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                User user = userDetails.getUser();
                userId = user.getId().toString();
            }
            AuditResourceType resourceType = auditable.resourceType();

            AuditLog auditLog = AuditLog.builder()
                    .actionType(auditable.actionType())
                    .payload(detailsJson)
                    .result(resultsJson)
                    .resourceType(resourceType)
                    .status(success ? AuditStatus.SUCCESS : AuditStatus.FAILURE)
                    .userId(userId)
                    .build();

            auditService.saveAuditLog(auditLog);
            if (caughtException != null) {
                throw caughtException;
            }

        }
        return result;
    }

    // Helper method to get the current username (copy from interceptor or a common utility)
    private String getCurrentUsername(HttpServletRequest request) {
        String username = request.getHeader("X-User-Id"); // Example for testing
        if (username == null || username.isEmpty()) {
            return "anonymous";
        }
        return username;
    }

    // Simple check to avoid logging large or sensitive objects. Extend as needed.
    private boolean isSensitiveType(Object obj) {
        return obj instanceof byte[] ||
                obj instanceof String && ((String) obj).length() > 5000 || // Prevent huge strings
                obj.getClass().getName().contains("password") ||
                obj.getClass().getName().contains("secret");
    }
}
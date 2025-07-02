package com.finance_tracker.exception.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finance_tracker.exception.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        // Register module to handle Java 8 date/time types (Instant, LocalDateTime, etc.)
        this.objectMapper.registerModule(new JavaTimeModule());
        // Optional: serialize dates as ISO strings, not timestamps
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        if (authException instanceof CustomAuthenticationException) {
            String json = objectMapper.writeValueAsString(((CustomAuthenticationException) authException).getUserReadablePayload());
            response.getWriter().write(json);
        } else {
            ApiError error = new ApiError(HttpStatus.UNAUTHORIZED.value(), "Authenication failed for some unknown reason", "Not authenticated");
            response.getWriter().write(objectMapper.writeValueAsString(error));
        }

    }
}

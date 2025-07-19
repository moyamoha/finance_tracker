package com.finance_tracker.mapper;

import com.finance_tracker.entity.User;
import com.finance_tracker.dto.requests.authentication.CreateUserRequest;

public class UserMapper {

    public static User toEntity(CreateUserRequest dto) {
        return User.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .password(dto.getPassword())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : null)
                .timezone(dto.getTimezone() != null ? dto.getTimezone() : null)
                .build();
    }

    public static CreateUserRequest toDto(User user) {
        return CreateUserRequest.builder()
                .lastName(user.getLastName())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .password(user.getPassword())
                .build();
    }
}

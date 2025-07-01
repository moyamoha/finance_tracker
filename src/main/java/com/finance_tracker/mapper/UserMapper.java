package com.finance_tracker.mapper;

import com.finance_tracker.entity.User;
import com.finance_tracker.dto.requests.CreateUserRequest;

public class UserMapper {

    public static User toEntity(CreateUserRequest dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static CreateUserRequest toDto(User user) {
        CreateUserRequest request = new CreateUserRequest();
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setEmail(user.getEmail());
        request.setPassword(user.getPassword());
        return request;
    }
}

package com.finance_tracker.service;

import com.finance_tracker.dto.requests.CreateUserRequest;
import com.finance_tracker.entity.User;
import com.finance_tracker.mapper.UserMapper;
import com.finance_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(CreateUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        User newUser = UserMapper.toEntity(dto);
        userRepository.save(newUser);
    }
}

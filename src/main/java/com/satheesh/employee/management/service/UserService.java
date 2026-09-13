package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.ChangePasswordRequest;
import com.satheesh.employee.management.dto.CreateUserRequest;
import com.satheesh.employee.management.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto createUser(
            CreateUserRequest request
    );

    void changePassword(
            String username,
            ChangePasswordRequest request
    );
}
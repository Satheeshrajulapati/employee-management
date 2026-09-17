package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.ChangePasswordRequest;
import com.satheesh.employee.management.dto.CreateUserRequest;
import com.satheesh.employee.management.dto.ResetUserPasswordRequest;
import com.satheesh.employee.management.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto createUser(
            CreateUserRequest request,
            String currentUsername
    );

    void changePassword(
            String username,
            ChangePasswordRequest request
    );

    UserResponseDto updateUserStatus(
            Long userId,
            String currentUsername,
            Boolean enabled
    );

    void resetUserPassword(
            Long userId,
            String currentUsername,
            ResetUserPasswordRequest request
    );
}
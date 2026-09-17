package com.satheesh.employee.management.controller;

import com.satheesh.employee.management.dto.CreateUserRequest;
import com.satheesh.employee.management.dto.ResetUserPasswordRequest;
import com.satheesh.employee.management.dto.UpdateUserStatusRequest;
import com.satheesh.employee.management.dto.UserResponseDto;
import org.springframework.security.core.Authentication;

import com.satheesh.employee.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody CreateUserRequest request,
            Authentication authentication
    ) {

        UserResponseDto createdUser =
                userService.createUser(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequest request,
            Authentication authentication
    ) {

        UserResponseDto updatedUser =
                userService.updateUserStatus(
                        userId,
                        authentication.getName(),
                        request.getEnabled()
                );

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<Void> resetUserPassword(
            @PathVariable Long userId,
            @Valid @RequestBody ResetUserPasswordRequest request,
            Authentication authentication
    ) {
        userService.resetUserPassword(
                userId,
                authentication.getName(),
                request
        );

        return ResponseEntity.noContent().build();
    }
}
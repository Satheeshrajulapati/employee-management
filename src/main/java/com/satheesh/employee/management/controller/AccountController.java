package com.satheesh.employee.management.controller;

import com.satheesh.employee.management.dto.ChangePasswordRequest;
import com.satheesh.employee.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserService userService;

    public AccountController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        String username = authentication.getName();

        userService.changePassword(
                username,
                request
        );

        return ResponseEntity.noContent().build();
    }
}
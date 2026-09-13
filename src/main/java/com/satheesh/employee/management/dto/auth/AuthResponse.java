package com.satheesh.employee.management.dto.auth;

import com.satheesh.employee.management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String message;
    private String token;
    private String username;
    private Role role;
    private boolean mustChangePassword;
}
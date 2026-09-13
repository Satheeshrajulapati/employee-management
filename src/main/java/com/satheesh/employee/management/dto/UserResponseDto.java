package com.satheesh.employee.management.dto;

import com.satheesh.employee.management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String username;

    private String email;

    private Role role;

    private boolean enabled;

    private boolean mustChangePassword;
}
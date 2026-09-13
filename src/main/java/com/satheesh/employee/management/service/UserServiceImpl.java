package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.ChangePasswordRequest;
import com.satheesh.employee.management.dto.CreateUserRequest;
import com.satheesh.employee.management.dto.ResetUserPasswordRequest;
import com.satheesh.employee.management.dto.UserResponseDto;
import com.satheesh.employee.management.entity.Role;
import com.satheesh.employee.management.entity.User;
import com.satheesh.employee.management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        return userRepository
                .findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public UserResponseDto createUser(
            CreateUserRequest request
    ) {

        if (userRepository.existsByUsername(
                request.getUsername()
        )) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(
                request.getEmail()
        )) {
            throw new IllegalArgumentException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getTemporaryPassword()
                        )
                )
                .role(Role.USER)
                .enabled(true)
                .mustChangePassword(true)
                .build();

        User savedUser =
                userRepository.save(user);

        return mapToDto(savedUser);
    }

    @Override
    public void changePassword(
            String username,
            ChangePasswordRequest request
    ) {

        // Find the currently logged-in user
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        // Verify the current password
        boolean currentPasswordMatches =
                passwordEncoder.matches(
                        request.getCurrentPassword(),
                        user.getPassword()
                );

        if (!currentPasswordMatches) {
            throw new RuntimeException(
                    "Current password is incorrect"
            );
        }

        // Don't allow the same password again
        boolean sameAsCurrentPassword =
                passwordEncoder.matches(
                        request.getNewPassword(),
                        user.getPassword()
                );

        if (sameAsCurrentPassword) {
            throw new RuntimeException(
                    "New password must be different from current password"
            );
        }

        // BCrypt encode the new password
        String encodedNewPassword =
                passwordEncoder.encode(
                        request.getNewPassword()
                );

        user.setPassword(encodedNewPassword);

        // First-time password change is now completed
        user.setMustChangePassword(false);

        userRepository.save(user);
    }

    private UserResponseDto mapToDto(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .mustChangePassword(
                        user.isMustChangePassword()
                )
                .build();
    }

    @Override
    public UserResponseDto updateUserStatus(
            Long userId,
            String currentUsername,
            Boolean enabled
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (user.getUsername().equals(currentUsername) && !enabled) {
            throw new IllegalArgumentException(
                    "You cannot disable your own account"
            );
        }

        user.setEnabled(enabled);

        User updatedUser = userRepository.save(user);

        return mapToDto(updatedUser);
    }

    @Override
    public void resetUserPassword(
            Long userId,
            String currentUsername,
            ResetUserPasswordRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException(
                    "You cannot reset your own password from User Management"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getTemporaryPassword()
                )
        );

        user.setMustChangePassword(true);

        userRepository.save(user);
    }

}
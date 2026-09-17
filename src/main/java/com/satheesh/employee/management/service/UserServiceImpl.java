package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.ChangePasswordRequest;
import com.satheesh.employee.management.dto.CreateUserRequest;
import com.satheesh.employee.management.dto.ResetUserPasswordRequest;
import com.satheesh.employee.management.dto.UserResponseDto;
import com.satheesh.employee.management.entity.Role;
import com.satheesh.employee.management.entity.User;
import com.satheesh.employee.management.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
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
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public UserResponseDto createUser(
            CreateUserRequest request,
            String currentUsername
    ) {
        User currentUser = getUserByUsername(currentUsername);

        validateCreatePermission(
                currentUser,
                request.getRole()
        );

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(request.getEmail())) {
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
                .role(request.getRole())
                .enabled(true)
                .mustChangePassword(true)
                .build();

        return mapToDto(
                userRepository.save(user)
        );
    }

    @Override
    public void changePassword(
            String username,
            ChangePasswordRequest request
    ) {
        User user = getUserByUsername(username);

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "Current password is incorrect"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setMustChangePassword(false);

        userRepository.save(user);
    }

    @Override
    public UserResponseDto updateUserStatus(
            Long userId,
            String currentUsername,
            Boolean enabled
    ) {
        User currentUser = getUserByUsername(currentUsername);
        User targetUser = getUserById(userId);

        if (currentUser.getId().equals(targetUser.getId())
                && !enabled) {
            throw new IllegalArgumentException(
                    "You cannot disable your own account"
            );
        }

        validateManagePermission(
                currentUser,
                targetUser
        );

        targetUser.setEnabled(enabled);

        return mapToDto(
                userRepository.save(targetUser)
        );
    }

    @Override
    public void resetUserPassword(
            Long userId,
            String currentUsername,
            ResetUserPasswordRequest request
    ) {
        User currentUser = getUserByUsername(currentUsername);
        User targetUser = getUserById(userId);

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new IllegalArgumentException(
                    "You cannot reset your own password from User Management"
            );
        }

        validateManagePermission(
                currentUser,
                targetUser
        );

        targetUser.setPassword(
                passwordEncoder.encode(
                        request.getTemporaryPassword()
                )
        );

        targetUser.setMustChangePassword(true);

        userRepository.save(targetUser);
    }

    private void validateCreatePermission(
            User currentUser,
            Role requestedRole
    ) {
        if (requestedRole == Role.SUPER_ADMIN) {
            throw new AccessDeniedException(
                    "SUPER_ADMIN cannot be created through User Management"
            );
        }

        if (currentUser.getRole() == Role.ADMIN
                && requestedRole != Role.USER) {
            throw new AccessDeniedException(
                    "ADMIN can create only USER accounts"
            );
        }

        if (currentUser.getRole() != Role.ADMIN
                && currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new AccessDeniedException(
                    "You do not have permission to create users"
            );
        }
    }

    private void validateManagePermission(
            User currentUser,
            User targetUser
    ) {
        if (targetUser.getRole() == Role.SUPER_ADMIN) {
            throw new AccessDeniedException(
                    "SUPER_ADMIN account cannot be managed from User Management"
            );
        }

        if (currentUser.getRole() == Role.ADMIN
                && targetUser.getRole() != Role.USER) {
            throw new AccessDeniedException(
                    "ADMIN can manage only USER accounts"
            );
        }

        if (currentUser.getRole() != Role.ADMIN
                && currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new AccessDeniedException(
                    "You do not have permission to manage users"
            );
        }
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );
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
}
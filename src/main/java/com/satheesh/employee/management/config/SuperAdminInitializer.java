package com.satheesh.employee.management.config;

import com.satheesh.employee.management.entity.User;
import com.satheesh.employee.management.entity.Role;
import com.satheesh.employee.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${super-admin.username}")
    private String username;

    @Value("${super-admin.email}")
    private String email;

    @Value("${super-admin.password}")
    private String password;

    @Override
    public void run(String... args) {

        if (userRepository.existsByRole(Role.SUPER_ADMIN)) {
            return;
        }

        User superAdmin = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.SUPER_ADMIN)
                .enabled(true)
                .mustChangePassword(false)
                .build();

        userRepository.save(superAdmin);
    }
}
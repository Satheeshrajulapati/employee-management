package com.satheesh.employee.management.repository;

import com.satheesh.employee.management.entity.Role;
import com.satheesh.employee.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    long countByEnabledTrue();

    long countByRoleIn(Collection<Role> roles);
}
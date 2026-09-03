package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.auth.AuthResponse;
import com.satheesh.employee.management.dto.auth.LoginRequest;
import com.satheesh.employee.management.dto.auth.RegisterRequest;
import com.satheesh.employee.management.entity.Role;
import com.satheesh.employee.management.entity.User;
import com.satheesh.employee.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return new AuthResponse(
                "User registered successfully",
                null
        );
    }

    public AuthResponse login(LoginRequest request){
      User user = userRepository.findByUsername(request.getUsername())
              .orElseThrow(() -> new RuntimeException("Invalid username or password"));

       boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

       if(!passwordMatches) {
           throw new RuntimeException("Invalid username or password!");
       }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new AuthResponse(
                "Login successful",
                token
        );
    }
}

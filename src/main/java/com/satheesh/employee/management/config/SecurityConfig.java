package com.satheesh.employee.management.config;

import com.satheesh.employee.management.security.CustomAccessDeniedHandler;
import com.satheesh.employee.management.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .anonymous(anonymous -> anonymous.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/error",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/dashboard/**")
                        .authenticated()

                        // User Management
                        .requestMatchers("/api/admin/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // Employee export
                        .requestMatchers(HttpMethod.GET, "/api/employees/export")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // Employee read access
                        .requestMatchers(HttpMethod.GET, "/api/employees/**")
                        .hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")

                        // Employee create access
                        .requestMatchers(HttpMethod.POST, "/api/employees/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // Employee update access
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // Employee delete access
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // All remaining APIs require authentication
                        .anyRequest()
                        .authenticated()
                );

        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
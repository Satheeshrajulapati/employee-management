package com.satheesh.employee.management.config;

import com.satheesh.employee.management.entity.User;
import com.satheesh.employee.management.repository.UserRepository;
import com.satheesh.employee.management.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.extractClaims(token);

            String username = claims.getSubject();

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new IllegalArgumentException("User not found"));

                if (!user.isEnabled()) {
                    SecurityContextHolder.clearContext();
                    writeErrorResponse(
                            response,
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "User account is disabled"
                    );
                    return;
                }

                if (user.isMustChangePassword() &&
                        !isChangePasswordRequest(request)) {

                    SecurityContextHolder.clearContext();
                    writeErrorResponse(
                            response,
                            HttpServletResponse.SC_FORBIDDEN,
                            "Password change is required before accessing this resource"
                    );
                    return;
                }

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(authority)
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();

        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isChangePasswordRequest(
            HttpServletRequest request
    ) {
        return request.getRequestURI()
                .equals("/api/account/change-password")
                && request.getMethod().equalsIgnoreCase("PUT");
    }

    private void writeErrorResponse(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
                """
                {
                  "message": "%s"
                }
                """.formatted(message)
        );
    }
}
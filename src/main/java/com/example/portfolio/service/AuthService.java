package com.example.portfolio.service;

import com.example.portfolio.auth.AdminPrincipal;
import com.example.portfolio.model.dto.AdminUserResponse;
import com.example.portfolio.model.dto.AuthResponse;
import com.example.portfolio.model.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password())
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        httpRequest.getSession(true)
                .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        AdminPrincipal principal = (AdminPrincipal) authentication.getPrincipal();
        return new AuthResponse("Login successful", new AdminUserResponse(principal.getId(), principal.getUsername(), true));
    }

    public void logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
    }

    public AdminUserResponse me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AdminPrincipal principal)) {
            return new AdminUserResponse(null, null, false);
        }
        return new AdminUserResponse(principal.getId(), principal.getUsername(), true);
    }
}

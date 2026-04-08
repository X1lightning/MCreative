package com.example.portfolio.controller;

import com.example.portfolio.model.dto.ApiMessageResponse;
import com.example.portfolio.model.dto.AuthResponse;
import com.example.portfolio.model.dto.LoginRequest;
import com.example.portfolio.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authService.login(request, httpRequest);
    }

    @PostMapping("/logout")
    public ApiMessageResponse logout(HttpServletRequest request) {
        authService.logout(request);
        return new ApiMessageResponse("Logged out");
    }

    @GetMapping("/me")
    public AuthResponse me(Authentication authentication) {
        return new AuthResponse("Current admin", authService.me(authentication));
    }
}

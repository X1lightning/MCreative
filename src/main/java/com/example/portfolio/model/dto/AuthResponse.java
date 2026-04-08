package com.example.portfolio.model.dto;

public record AuthResponse(
        String message,
        AdminUserResponse user
) {
}

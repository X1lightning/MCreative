package com.example.portfolio.model.dto;

public record AdminUserResponse(
        Long id,
        String username,
        boolean authenticated
) {
}

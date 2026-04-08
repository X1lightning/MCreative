package com.example.portfolio.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

public record ProjectImageUpdateRequest(
        @Size(max = 255) String altText,
        @Max(9999) Integer displayOrder
) {
}

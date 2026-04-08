package com.example.portfolio.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ProjectRequest(
        @NotBlank @Size(max = 160) String title,
        @NotBlank @Size(max = 180)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must contain lowercase letters, numbers, and hyphens only")
        String slug,
        @NotBlank @Size(max = 120) String category,
        @NotBlank @Size(max = 4000) String description,
        @Size(max = 500) String toolsUsed,
        @Size(max = 255) String createdFor,
        @PastOrPresent LocalDate projectDate,
        boolean featured,
        boolean published,
        @NotNull @Max(9999) Integer sortOrder
) {
}

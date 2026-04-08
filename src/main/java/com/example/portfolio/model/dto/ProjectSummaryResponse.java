package com.example.portfolio.model.dto;

import java.time.LocalDate;

public record ProjectSummaryResponse(
        Long id,
        String title,
        String slug,
        String category,
        String description,
        String toolsUsed,
        String createdFor,
        LocalDate projectDate,
        boolean featured,
        boolean published,
        Integer sortOrder,
        ProjectImageResponse coverImage
) {
}

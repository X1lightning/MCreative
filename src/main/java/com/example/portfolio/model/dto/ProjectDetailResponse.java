package com.example.portfolio.model.dto;

import java.time.LocalDate;
import java.util.List;

public record ProjectDetailResponse(
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
        ProjectImageResponse coverImage,
        List<ProjectImageResponse> images
) {
}

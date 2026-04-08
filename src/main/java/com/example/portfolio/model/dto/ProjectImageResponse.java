package com.example.portfolio.model.dto;

public record ProjectImageResponse(
        Long id,
        String url,
        String fileName,
        String originalFileName,
        String altText,
        Integer displayOrder,
        boolean cover
) {
}

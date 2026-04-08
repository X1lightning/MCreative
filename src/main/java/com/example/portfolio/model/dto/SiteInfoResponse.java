package com.example.portfolio.model.dto;

public record SiteInfoResponse(
        String siteName,
        String designerName,
        String tagline,
        String aboutSnippet,
        String email,
        String linkedinUrl,
        String instagramUrl
) {
}

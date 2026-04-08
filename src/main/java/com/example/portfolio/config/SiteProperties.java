package com.example.portfolio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.site")
public record SiteProperties(
        String name,
        String designerName,
        String tagline,
        String aboutSnippet,
        String email,
        String linkedinUrl,
        String instagramUrl
) {
}

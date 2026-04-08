package com.example.portfolio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        String uploadDir,
        String publicUrlPrefix,
        String publicUrlBase,
        long maxImageSizeBytes
) {
}

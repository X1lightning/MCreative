package com.example.portfolio.service;

import com.example.portfolio.config.SiteProperties;
import com.example.portfolio.model.dto.SiteInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class SiteService {

    private final SiteProperties siteProperties;

    public SiteService(SiteProperties siteProperties) {
        this.siteProperties = siteProperties;
    }

    public SiteInfoResponse getSiteInfo() {
        return new SiteInfoResponse(
                siteProperties.name(),
                siteProperties.designerName(),
                siteProperties.tagline(),
                siteProperties.aboutSnippet(),
                siteProperties.email(),
                siteProperties.linkedinUrl(),
                siteProperties.instagramUrl()
        );
    }
}

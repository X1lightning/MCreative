package com.example.portfolio.controller;

import com.example.portfolio.model.dto.CategoryListResponse;
import com.example.portfolio.model.dto.ProjectDetailResponse;
import com.example.portfolio.model.dto.ProjectListResponse;
import com.example.portfolio.model.dto.SiteInfoResponse;
import com.example.portfolio.service.ProjectService;
import com.example.portfolio.service.SiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicSiteController {

    private final SiteService siteService;
    private final ProjectService projectService;

    public PublicSiteController(SiteService siteService, ProjectService projectService) {
        this.siteService = siteService;
        this.projectService = projectService;
    }

    @GetMapping("/site")
    public SiteInfoResponse getSiteInfo() {
        return siteService.getSiteInfo();
    }

    @GetMapping("/projects")
    public ProjectListResponse getProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false, defaultValue = "true") Boolean published
    ) {
        return projectService.getPublicProjects(category, featured, published);
    }

    @GetMapping("/projects/{slug}")
    public ProjectDetailResponse getProject(@PathVariable String slug) {
        return projectService.getPublicProject(slug);
    }

    @GetMapping("/categories")
    public CategoryListResponse getCategories() {
        return new CategoryListResponse(projectService.getPublishedCategories());
    }
}

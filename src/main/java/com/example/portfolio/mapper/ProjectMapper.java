package com.example.portfolio.mapper;

import com.example.portfolio.config.StorageProperties;
import com.example.portfolio.model.dto.ProjectDetailResponse;
import com.example.portfolio.model.dto.ProjectImageResponse;
import com.example.portfolio.model.dto.ProjectRequest;
import com.example.portfolio.model.dto.ProjectSummaryResponse;
import com.example.portfolio.model.entity.Project;
import com.example.portfolio.model.entity.ProjectImage;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    private final StorageProperties storageProperties;

    public ProjectMapper(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    public void apply(ProjectRequest request, Project project) {
        project.setTitle(request.title().trim());
        project.setSlug(request.slug().trim());
        project.setCategory(request.category().trim());
        project.setDescription(request.description().trim());
        project.setToolsUsed(trimToNull(request.toolsUsed()));
        project.setCreatedFor(trimToNull(request.createdFor()));
        project.setProjectDate(request.projectDate());
        project.setFeatured(request.featured());
        project.setPublished(request.published());
        project.setSortOrder(request.sortOrder());
    }

    public ProjectSummaryResponse toSummary(Project project) {
        return new ProjectSummaryResponse(
                project.getId(),
                project.getTitle(),
                project.getSlug(),
                project.getCategory(),
                project.getDescription(),
                project.getToolsUsed(),
                project.getCreatedFor(),
                project.getProjectDate(),
                project.isFeatured(),
                project.isPublished(),
                project.getSortOrder(),
                project.getImages().stream().filter(ProjectImage::isCover).findFirst().map(this::toImage).orElse(null)
        );
    }

    public ProjectDetailResponse toDetail(Project project) {
        List<ProjectImageResponse> images = project.getImages().stream()
                .sorted(Comparator.comparing(ProjectImage::getDisplayOrder).thenComparing(ProjectImage::getId))
                .map(this::toImage)
                .toList();
        ProjectImageResponse cover = images.stream().filter(ProjectImageResponse::cover).findFirst().orElse(images.isEmpty() ? null : images.getFirst());
        return new ProjectDetailResponse(
                project.getId(),
                project.getTitle(),
                project.getSlug(),
                project.getCategory(),
                project.getDescription(),
                project.getToolsUsed(),
                project.getCreatedFor(),
                project.getProjectDate(),
                project.isFeatured(),
                project.isPublished(),
                project.getSortOrder(),
                cover,
                images
        );
    }

    public ProjectImageResponse toImage(ProjectImage image) {
        String normalizedBase = storageProperties.publicUrlBase().endsWith("/")
                ? storageProperties.publicUrlBase().substring(0, storageProperties.publicUrlBase().length() - 1)
                : storageProperties.publicUrlBase();
        return new ProjectImageResponse(
                image.getId(),
                normalizedBase + "/" + image.getFilePath().replace("\\", "/"),
                image.getFileName(),
                image.getOriginalFileName(),
                image.getAltText(),
                image.getDisplayOrder(),
                image.isCover()
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

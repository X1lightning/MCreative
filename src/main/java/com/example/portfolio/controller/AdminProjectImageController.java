package com.example.portfolio.controller;

import com.example.portfolio.model.dto.ApiMessageResponse;
import com.example.portfolio.model.dto.ImageReorderRequest;
import com.example.portfolio.model.dto.ProjectDetailResponse;
import com.example.portfolio.model.dto.ProjectImageResponse;
import com.example.portfolio.model.dto.ProjectImageUpdateRequest;
import com.example.portfolio.service.ProjectImageService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/projects/{projectId}/images")
public class AdminProjectImageController {

    private final ProjectImageService projectImageService;

    public AdminProjectImageController(ProjectImageService projectImageService) {
        this.projectImageService = projectImageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProjectImageResponse upload(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText", required = false) String altText
    ) {
        return projectImageService.upload(projectId, file, altText);
    }

    @PutMapping("/{imageId}")
    public ProjectImageResponse update(
            @PathVariable Long projectId,
            @PathVariable Long imageId,
            @Valid @org.springframework.web.bind.annotation.RequestBody ProjectImageUpdateRequest request
    ) {
        return projectImageService.update(projectId, imageId, request);
    }

    @DeleteMapping("/{imageId}")
    public ApiMessageResponse delete(@PathVariable Long projectId, @PathVariable Long imageId) {
        projectImageService.delete(projectId, imageId);
        return new ApiMessageResponse("Image deleted");
    }

    @PutMapping("/reorder")
    public List<ProjectImageResponse> reorder(
            @PathVariable Long projectId,
            @Valid @org.springframework.web.bind.annotation.RequestBody ImageReorderRequest request
    ) {
        return projectImageService.reorder(projectId, request);
    }

    @PutMapping("/{imageId}/set-cover")
    public ProjectDetailResponse setCover(@PathVariable Long projectId, @PathVariable Long imageId) {
        return projectImageService.setCover(projectId, imageId);
    }
}

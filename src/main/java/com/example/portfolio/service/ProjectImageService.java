package com.example.portfolio.service;

import com.example.portfolio.exception.NotFoundException;
import com.example.portfolio.mapper.ProjectMapper;
import com.example.portfolio.model.dto.ImageReorderRequest;
import com.example.portfolio.model.dto.ProjectDetailResponse;
import com.example.portfolio.model.dto.ProjectImageResponse;
import com.example.portfolio.model.dto.ProjectImageUpdateRequest;
import com.example.portfolio.model.entity.Project;
import com.example.portfolio.model.entity.ProjectImage;
import com.example.portfolio.repository.ProjectImageRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProjectImageService {

    private final ProjectService projectService;
    private final ProjectImageRepository projectImageRepository;
    private final ImageStorageService imageStorageService;
    private final ProjectMapper projectMapper;

    public ProjectImageService(
            ProjectService projectService,
            ProjectImageRepository projectImageRepository,
            ImageStorageService imageStorageService,
            ProjectMapper projectMapper
    ) {
        this.projectService = projectService;
        this.projectImageRepository = projectImageRepository;
        this.imageStorageService = imageStorageService;
        this.projectMapper = projectMapper;
    }

    @Transactional
    public ProjectImageResponse upload(Long projectId, MultipartFile file, String altText) {
        Project project = projectService.getProjectEntity(projectId);
        List<ProjectImage> existingImages = projectImageRepository.findAllByProjectIdOrderByDisplayOrderAscIdAsc(projectId);
        ImageStorageService.StoredImage storedImage = imageStorageService.store(
                project,
                file,
                existingImages.isEmpty() ? "cover" : "detail"
        );

        ProjectImage image = new ProjectImage();
        image.setProject(project);
        image.setFileName(storedImage.fileName());
        image.setOriginalFileName(storedImage.originalFileName());
        image.setFilePath(storedImage.relativePath());
        image.setAltText(altText == null ? null : altText.trim());
        image.setDisplayOrder(existingImages.size());
        image.setCover(existingImages.isEmpty());
        return projectMapper.toImage(projectImageRepository.save(image));
    }

    @Transactional
    public ProjectImageResponse update(Long projectId, Long imageId, ProjectImageUpdateRequest request) {
        ProjectImage image = getImage(projectId, imageId);
        image.setAltText(request.altText() == null ? null : request.altText().trim());
        if (request.displayOrder() != null) {
            image.setDisplayOrder(request.displayOrder());
        }
        return projectMapper.toImage(projectImageRepository.save(image));
    }

    @Transactional
    public void delete(Long projectId, Long imageId) {
        ProjectImage image = getImage(projectId, imageId);
        boolean wasCover = image.isCover();
        projectImageRepository.delete(image);
        imageStorageService.deleteIfExists(image.getFilePath());

        List<ProjectImage> remainingImages = projectImageRepository.findAllByProjectIdOrderByDisplayOrderAscIdAsc(projectId);
        if (wasCover && !remainingImages.isEmpty()) {
            remainingImages.getFirst().setCover(true);
        }
        normalizeOrders(remainingImages);
    }

    @Transactional
    public List<ProjectImageResponse> reorder(Long projectId, ImageReorderRequest request) {
        List<ProjectImage> images = projectImageRepository.findAllByProjectIdOrderByDisplayOrderAscIdAsc(projectId);
        Map<Long, Integer> targetOrder = new HashMap<>();
        request.items().forEach(item -> targetOrder.put(item.imageId(), item.displayOrder()));
        images.forEach(image -> {
            Integer order = targetOrder.get(image.getId());
            if (order != null) {
                image.setDisplayOrder(order);
            }
        });
        normalizeOrders(images);
        return images.stream().map(projectMapper::toImage).toList();
    }

    @Transactional
    public ProjectDetailResponse setCover(Long projectId, Long imageId) {
        Project project = projectService.getProjectEntity(projectId);
        project.getImages().forEach(image -> image.setCover(image.getId().equals(imageId)));
        return projectMapper.toDetail(project);
    }

    private void normalizeOrders(List<ProjectImage> images) {
        AtomicInteger index = new AtomicInteger();
        images.stream()
                .sorted((left, right) -> Integer.compare(left.getDisplayOrder(), right.getDisplayOrder()))
                .forEachOrdered(image -> image.setDisplayOrder(index.getAndIncrement()));
    }

    private ProjectImage getImage(Long projectId, Long imageId) {
        return projectImageRepository.findByIdAndProjectId(imageId, projectId)
                .orElseThrow(() -> new NotFoundException("Project image not found"));
    }
}

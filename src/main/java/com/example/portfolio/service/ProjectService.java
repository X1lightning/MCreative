package com.example.portfolio.service;

import com.example.portfolio.exception.ConflictException;
import com.example.portfolio.exception.NotFoundException;
import com.example.portfolio.mapper.ProjectMapper;
import com.example.portfolio.model.dto.ProjectDetailResponse;
import com.example.portfolio.model.dto.ProjectListResponse;
import com.example.portfolio.model.dto.ProjectRequest;
import com.example.portfolio.model.entity.Project;
import com.example.portfolio.repository.ProjectRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional(readOnly = true)
    public ProjectListResponse getPublicProjects(String category, Boolean featured, Boolean published) {
        List<Project> projects = projectRepository.findAllPublishedWithImages().stream()
                .filter(project -> category == null || project.getCategory().equalsIgnoreCase(category))
                .filter(project -> featured == null || project.isFeatured() == featured)
                .filter(project -> published == null || project.isPublished() == published)
                .sorted(Comparator.comparing(Project::isFeatured).reversed()
                        .thenComparing(Project::getSortOrder)
                        .thenComparing(Project::getProjectDate, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Project::getId, Comparator.reverseOrder()))
                .map(project -> project)
                .toList();
        return new ProjectListResponse(projects.stream().map(projectMapper::toSummary).toList());
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getPublicProject(String slug) {
        Project project = projectRepository.findPublishedDetailBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        return projectMapper.toDetail(project);
    }

    @Transactional(readOnly = true)
    public List<String> getPublishedCategories() {
        return projectRepository.findDistinctPublishedCategories();
    }

    @Transactional(readOnly = true)
    public ProjectListResponse getAdminProjects() {
        return new ProjectListResponse(projectRepository.findAllWithImagesOrderBySortOrder().stream()
                .map(projectMapper::toSummary)
                .toList());
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getAdminProject(Long id) {
        return projectMapper.toDetail(getProjectEntity(id));
    }

    @Transactional
    public ProjectDetailResponse createProject(ProjectRequest request) {
        ensureUniqueSlug(request.slug(), null);
        Project project = new Project();
        projectMapper.apply(request, project);
        return projectMapper.toDetail(projectRepository.save(project));
    }

    @Transactional
    public ProjectDetailResponse updateProject(Long id, ProjectRequest request) {
        ensureUniqueSlug(request.slug(), id);
        Project project = getProjectEntity(id);
        projectMapper.apply(request, project);
        return projectMapper.toDetail(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = getProjectEntity(id);
        projectRepository.delete(project);
    }

    @Transactional(readOnly = true)
    public Project getProjectEntity(Long id) {
        return projectRepository.findDetailById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }

    private void ensureUniqueSlug(String slug, Long existingId) {
        boolean exists = existingId == null
                ? projectRepository.existsBySlug(slug)
                : projectRepository.existsBySlugAndIdNot(slug, existingId);
        if (exists) {
            throw new ConflictException("Slug is already in use");
        }
    }
}

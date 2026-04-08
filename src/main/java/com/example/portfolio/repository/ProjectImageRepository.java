package com.example.portfolio.repository;

import com.example.portfolio.model.entity.ProjectImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    List<ProjectImage> findAllByProjectIdOrderByDisplayOrderAscIdAsc(Long projectId);

    Optional<ProjectImage> findByIdAndProjectId(Long imageId, Long projectId);

    Optional<ProjectImage> findByProjectIdAndCoverTrue(Long projectId);
}

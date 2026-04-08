package com.example.portfolio.repository;

import com.example.portfolio.model.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "images")
    @Query("select p from Project p where p.id = :id")
    Optional<Project> findDetailById(Long id);

    @EntityGraph(attributePaths = "images")
    @Query("select p from Project p where p.slug = :slug and p.published = true")
    Optional<Project> findPublishedDetailBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @EntityGraph(attributePaths = "images")
    @Query("select p from Project p where p.published = true")
    List<Project> findAllPublishedWithImages();

    @EntityGraph(attributePaths = "images")
    @Query("select p from Project p")
    List<Project> findAllWithImagesOrderBySortOrder();

    @Query("select distinct p.category from Project p where p.published = true order by p.category asc")
    List<String> findDistinctPublishedCategories();
}

package com.example.portfolio.service;

import com.example.portfolio.config.StorageProperties;
import com.example.portfolio.exception.BadRequestException;
import com.example.portfolio.exception.FileStorageException;
import com.example.portfolio.model.entity.Project;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    private final StorageProperties storageProperties;

    public ImageStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    public StoredImage store(Project project, MultipartFile file, String labelPrefix) {
        validate(file);
        Path projectDirectory = Path.of(storageProperties.uploadDir(), "projects", project.getSlug()).normalize();
        String extension = detectExtension(file);
        String safeFileName = labelPrefix + "-" + UUID.randomUUID() + extension;
        Path destination = projectDirectory.resolve(safeFileName).normalize();

        try {
            Files.createDirectories(projectDirectory);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store image on disk", ex);
        }

        String relativePath = Path.of("projects", project.getSlug(), safeFileName).toString();
        return new StoredImage(safeFileName, sanitizeOriginalFilename(file.getOriginalFilename()), relativePath);
    }

    public void deleteIfExists(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(Path.of(storageProperties.uploadDir()).resolve(relativePath).normalize());
        } catch (IOException ex) {
            throw new FileStorageException("Failed to delete image from disk", ex);
        }
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file is empty");
        }
        if (file.getSize() > storageProperties.maxImageSizeBytes()) {
            throw new BadRequestException("Image exceeds the configured file size limit");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Only JPG, JPEG, and PNG uploads are allowed");
        }
    }

    private String detectExtension(MultipartFile file) {
        String contentType = file.getContentType();
        return "image/png".equals(contentType) ? ".png" : ".jpg";
    }

    private String sanitizeOriginalFilename(String originalFilename) {
        String cleaned = StringUtils.cleanPath(originalFilename == null ? "upload.jpg" : originalFilename);
        return cleaned.replaceAll("[^a-zA-Z0-9._-]", "-");
    }

    public record StoredImage(String fileName, String originalFileName, String relativePath) {
    }
}

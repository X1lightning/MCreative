package com.example.portfolio.config;

import java.nio.file.Path;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({CorsProperties.class, SiteProperties.class, SeedProperties.class})
public class WebConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;
    private final StorageProperties storageProperties;

    public WebConfig(CorsProperties corsProperties, StorageProperties storageProperties) {
        this.corsProperties = corsProperties;
        this.storageProperties = storageProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(corsProperties.allowedOrigins().toArray(String[]::new))
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Path.of(storageProperties.uploadDir()).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler(storageProperties.publicUrlPrefix())
                .addResourceLocations(location);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring:^(?!api|uploads|assets).*$}")
                .setViewName("forward:/index.html");
        registry.addViewController("/**/{spring:^(?!api|uploads|assets).*$}")
                .setViewName("forward:/index.html");
    }
}

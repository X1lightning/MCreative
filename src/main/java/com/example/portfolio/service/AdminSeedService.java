package com.example.portfolio.service;

import com.example.portfolio.config.SeedProperties;
import com.example.portfolio.model.entity.AdminUser;
import com.example.portfolio.repository.AdminUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeedService implements ApplicationRunner {

    private final SeedProperties seedProperties;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeedService(
            SeedProperties seedProperties,
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.seedProperties = seedProperties;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!seedProperties.enabled() || adminUserRepository.findByUsername(seedProperties.adminUsername()).isPresent()) {
            return;
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(seedProperties.adminUsername());
        adminUser.setPasswordHash(passwordEncoder.encode(seedProperties.adminPassword()));
        adminUserRepository.save(adminUser);
    }
}

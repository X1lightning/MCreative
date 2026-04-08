package com.example.portfolio.auth;

import com.example.portfolio.model.entity.AdminUser;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminPrincipal implements UserDetails {

    private final AdminUser adminUser;

    public AdminPrincipal(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Long getId() {
        return adminUser.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return adminUser.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return adminUser.getUsername();
    }
}

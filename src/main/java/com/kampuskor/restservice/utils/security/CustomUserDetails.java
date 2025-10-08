package com.kampuskor.restservice.utils.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kampuskor.restservice.features.User.enums.RoleType;
import com.kampuskor.restservice.features.User.User;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = switch (user.getRoleType()) {
            case RoleType.A -> "ROLE_ADMIN";
            case RoleType.I -> "ROLE_INSTRUCTOR";
            case RoleType.S -> "ROLE_STUDENT";
        };
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }
}

package com.kampuskor.restservice.features.User.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kampuskor.restservice.features.User.enums.RoleType;

public class CreateUserRequest {
    private String name;
    private String username;
    private String email;
    private String password;

    @JsonProperty("role_type")
    private RoleType roleType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}

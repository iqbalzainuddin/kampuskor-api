package com.kampuskor.restservice.features.User.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kampuskor.restservice.features.User.enums.RoleType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email,

    @JsonProperty("role_type")
    @NotBlank(message = "Role type is required")
    RoleType roleType
) {
}

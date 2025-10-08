package com.kampuskor.restservice.features.Auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateProfileRequest(
    @NotNull(message = "Name is required")
    String name,
    @NotNull(message = "Username is required")
    String username,
    @NotNull(message = "Email is required") @Email
    String email
) {
}

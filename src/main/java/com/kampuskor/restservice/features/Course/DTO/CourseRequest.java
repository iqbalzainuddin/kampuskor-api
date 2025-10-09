package com.kampuskor.restservice.features.Course.DTO;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Course Code is required")
    String code,

    String description
) {
}

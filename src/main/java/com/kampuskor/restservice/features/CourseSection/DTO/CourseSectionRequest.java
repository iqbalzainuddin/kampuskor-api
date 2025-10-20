package com.kampuskor.restservice.features.CourseSection.DTO;

import jakarta.validation.constraints.NotBlank;

public record CourseSectionRequest(
    @NotBlank(message = "Name is required")
    String name,
    
    String description
) {
}

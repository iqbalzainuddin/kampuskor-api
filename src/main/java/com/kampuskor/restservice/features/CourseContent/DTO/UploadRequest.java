package com.kampuskor.restservice.features.CourseContent.DTO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadRequest(
    @NotNull(message = "File is required")
    MultipartFile file,

    @NotBlank(message = "Display name is required")
    String displayName
) {    
}

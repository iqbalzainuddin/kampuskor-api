package com.kampuskor.restservice.features.Course.DTO;

import java.time.LocalDateTime;

public record CourseResponse(
    Long id,
    String name,
    String code,
    String description,
    Long instructorId,
    String instructorName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

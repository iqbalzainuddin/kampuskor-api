package com.kampuskor.restservice.features.Course.DTO;

import java.time.LocalDateTime;
import java.util.Set;

import com.kampuskor.restservice.features.User.User;

public record CourseStudentsResponse(
    Long id,
    String name,
    String code,
    String description,
    Long instructorId,
    String instructorName,
    Set<User> students,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {   
}

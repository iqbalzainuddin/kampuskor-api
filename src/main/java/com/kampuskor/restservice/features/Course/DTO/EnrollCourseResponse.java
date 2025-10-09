package com.kampuskor.restservice.features.Course.DTO;

public record EnrollCourseResponse(
    Long courseId,
    String courseName,
    Long studentId,
    String studentName
) {
}

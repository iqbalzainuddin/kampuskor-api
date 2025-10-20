package com.kampuskor.restservice.features.CourseSection;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    Page<CourseSection> findByCourseId(Long courseId, Pageable pageable);
    Optional<CourseSection> findByIdAndCourseIdAndInstructorId(
        Long id,
        Long courseId,
        Long instructorId
    );

    Boolean existsByIdAndCourseId(Long id, Long courseId);
    Boolean existsByIdAndCourseIdAndInstructorId(Long id, Long courseId, Long instructorId);
}

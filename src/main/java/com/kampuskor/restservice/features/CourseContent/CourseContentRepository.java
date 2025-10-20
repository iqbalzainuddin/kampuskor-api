package com.kampuskor.restservice.features.CourseContent;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    Page<CourseContent> findByCourseIdAndSectionId(
        Long courseId,
        Long sectionId,
        Pageable pageable
    );
    Optional<CourseContent> findByIdAndCourseIdAndSectionIdAndInstructorId(
        Long id, 
        Long courseId, 
        Long sectionId, 
        Long instructorId
    );
}

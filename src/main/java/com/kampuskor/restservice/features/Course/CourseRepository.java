package com.kampuskor.restservice.features.Course;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kampuskor.restservice.features.Course.Model.Course;
import com.kampuskor.restservice.features.User.User;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCode(String code);
    Page<Course> findByInstructor(User instructor, PageRequest pageRequest);
    Page<Course> findByStudents(User student, PageRequest pageRequest);
    Optional<Course> findByIdAndInstructor(Long id, User instructor);

    Boolean existsByIdAndInstructorId(Long id, Long instructorId);
    Boolean existsByIdAndStudentsId(Long id, Long studentId);

    @Query("""
        SELECT s
        FROM Course c 
        JOIN c.students s
        WHERE c.id = :courseId 
        AND c.instructor = :instructor
    """)
    Page<User> findStudentsByIdAndInstructor(
        @Param("courseId") Long courseId, 
        @Param("instructor") User instructor, 
        PageRequest pageRequest
    );
}

package com.kampuskor.restservice.features.CourseSection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kampuskor.restservice.features.Course.CourseRepository;
import com.kampuskor.restservice.features.Course.Model.Course;
import com.kampuskor.restservice.features.CourseSection.DTO.CourseSectionRequest;
import com.kampuskor.restservice.features.User.User;
import com.kampuskor.restservice.features.User.UserRepository;

@Service
public class CourseSectionService {
    private final CourseSectionRepository courseSectionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseSectionService(CourseSectionRepository courseSectionRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.courseSectionRepository = courseSectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public CourseSection createCourseSection(
        CourseSectionRequest newCourseSection,
        Long courseId,
        String instructorUsername
    ) {
        User instructor = userRepository.findByUsername(instructorUsername)
            .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        Course course = courseRepository.findByIdAndInstructor(courseId, instructor)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        CourseSection courseSection = new CourseSection();
        courseSection.setName(newCourseSection.name());
        courseSection.setDescription(newCourseSection.description());
        courseSection.setInstructor(instructor);
        courseSection.setCourse(course);

        return courseSectionRepository.save(courseSection);
    }

    public Page<CourseSection> getCourseSectionsByCourse(
        Long courseId,
        Long authenticatedUserId,
        String authenticatedRole,
        int page,
        int size,
        Sort sort
    ) {
        if (authenticatedRole.equals("ROLE_STUDENT")) {
            Boolean isEnrolled = courseRepository.existsByIdAndStudentsId(courseId, authenticatedUserId);
            if (!isEnrolled) {
                throw new IllegalArgumentException("Enrolled course not found");
            }
        }
        
        if (authenticatedRole.equals("ROLE_INSTRUCTOR")) {
            Boolean isInstructor = courseRepository.existsByIdAndInstructorId(courseId, authenticatedUserId);
            System.out.println(isInstructor);
            if (!isInstructor) {
                throw new IllegalArgumentException("Course not found");
            }
        }
        
        return courseSectionRepository.findByCourseId(
            courseId,
            PageRequest.of(page, size, sort)
        );
    }

    public CourseSection getCourseSectionByIdAndCourseAndInstructor(
        Long sectionId,
        Long courseId,
        Long instructorId
    ) {
        return courseSectionRepository.findByIdAndCourseIdAndInstructorId(
            sectionId,
            courseId,
            instructorId
        ).orElseThrow(() -> new IllegalArgumentException("Course section not found or access denied"));
    }

    public void updateCourseSection(
        Long courseId,
        Long sectionId,
        CourseSectionRequest courseSectionRequest,
        Long instructorId
    ) {
        if (!courseSectionRepository.existsByIdAndCourseIdAndInstructorId(
            sectionId,
            courseId,
            instructorId
        )) {
            throw new IllegalArgumentException("Course section not found or access denied");  
        }

        CourseSection courseSection = courseSectionRepository.findById(sectionId)
            .orElseThrow(() -> new IllegalArgumentException("Course section not found"));

        courseSection.setName(courseSectionRequest.name());
        courseSection.setDescription(courseSectionRequest.description());
        
        courseSectionRepository.save(courseSection);
    }

    public void deleteCourseSection(
        Long courseId,
        Long sectionId,
        Long instructorId
    ) {
        if (!courseSectionRepository.existsByIdAndCourseIdAndInstructorId(
            sectionId,
            courseId,
            instructorId
        )) {
            throw new IllegalArgumentException("Course section not found or access denied");
        }

        courseSectionRepository.deleteById(sectionId);
    }
}

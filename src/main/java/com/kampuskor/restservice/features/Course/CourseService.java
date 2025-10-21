package com.kampuskor.restservice.features.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kampuskor.restservice.common.response.PageResponse;
import com.kampuskor.restservice.features.Course.DTO.CourseRequest;
import com.kampuskor.restservice.features.Course.DTO.CourseResponse;
import com.kampuskor.restservice.features.Course.DTO.EnrollCourseResponse;
import com.kampuskor.restservice.features.Course.Model.Course;
import com.kampuskor.restservice.features.User.User;
import com.kampuskor.restservice.features.User.UserRepository;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseResponse> findAll(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
            )
        );

        return new PageResponse<>(
            courses.getNumber(),
            courses.getNumberOfElements(),
            courses.getTotalPages(),
            courses.getTotalElements(),
            courses.stream().map(course -> new CourseResponse(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getDescription(),
                course.getInstructor().getId(),
                course.getInstructor().getName(),
                course.getCreatedAt(),
                course.getUpdatedAt()
            )).toList()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseResponse> getCoursesByInstructor(Pageable pageable, String instructorUsername) {
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        Page<Course> courses = courseRepository.findByInstructor(
            instructor,
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
            )
        );

        return new PageResponse<>(
            courses.getNumber(),
            courses.getNumberOfElements(),
            courses.getTotalPages(),
            courses.getTotalElements(),
            courses.stream().map(course -> new CourseResponse(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getDescription(),
                course.getInstructor().getId(),
                course.getInstructor().getName(),
                course.getCreatedAt(),
                course.getUpdatedAt()
            )).toList()
        );
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return new CourseResponse(
            course.getId(),
            course.getName(),
            course.getCode(),
            course.getDescription(),
            course.getInstructor().getId(),
            course.getInstructor().getName(),
            course.getCreatedAt(),
            course.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<User> getCourseStudents(Long courseId, Pageable pageable, String instructorUsername) {
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course not found");
        }
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        Page<User> students = courseRepository.findStudentsByIdAndInstructor(
            courseId, 
            instructor, 
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
            )
        );

        return new PageResponse<>(
            students.getNumber(),
            students.getNumberOfElements(),
            students.getTotalPages(),
            students.getTotalElements(),
            students.getContent()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseResponse> getEnrolledCourses(Pageable pageable, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Page<Course> courses = courseRepository.findByStudents(
            student,
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
            )
        );

        return new PageResponse<>(
            courses.getNumber(),
            courses.getNumberOfElements(),
            courses.getTotalPages(),
            courses.getTotalElements(),
            courses.stream().map(course -> new CourseResponse(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getDescription(),
                course.getInstructor().getId(),
                course.getInstructor().getName(),
                course.getCreatedAt(),
                course.getUpdatedAt()
            )).toList()
        );
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest course, String instructorUsername) {
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        Course newCourse = new Course();
        newCourse.setName(course.name());
        newCourse.setCode(course.code());
        newCourse.setDescription(course.description());
        newCourse.setInstructor(instructor);

        Course savedCourse = courseRepository.save(newCourse);

        return new CourseResponse(
            savedCourse.getId(),
            savedCourse.getName(),
            savedCourse.getCode(),
            savedCourse.getDescription(),
            savedCourse.getInstructor().getId(),
            savedCourse.getInstructor().getName(),
            savedCourse.getCreatedAt(),
            savedCourse.getUpdatedAt()
        );
    }

    @Transactional
    public EnrollCourseResponse enrollCourse(Long courseId, String studentUsername) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        course.getStudents().add(student);
        student.getEnrolledCourses().add(course);

        courseRepository.save(course);
        userRepository.save(student);

        return new EnrollCourseResponse(
            course.getId(),
            course.getName(),
            student.getId(),
            student.getName()
        );
    }

    @Transactional
    public void unenrollCourse(Long courseId, String studentUsername) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        course.getStudents().remove(student);
        student.getEnrolledCourses().remove(course);

        courseRepository.save(course);
        userRepository.save(student);
    }

    @Transactional
    public void updateCourse(Long courseId, CourseRequest courseRequest, String instructorUsername) {
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        Course course = courseRepository.findByIdAndInstructor(courseId, instructor)
                .orElseThrow(() -> new IllegalArgumentException("Course not found or you are not the instructor"));

        course.setName(courseRequest.name());
        course.setCode(courseRequest.code());
        course.setDescription(courseRequest.description());

        courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long courseId, String instructorUsername) {
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        Course course = courseRepository.findByIdAndInstructor(courseId, instructor)
                .orElseThrow(() -> new IllegalArgumentException("Course not found or you are not the instructor"));

        courseRepository.delete(course);
    }
}

package com.kampuskor.restservice.features.Course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kampuskor.restservice.common.response.PageResponse;
import com.kampuskor.restservice.features.Course.DTO.CourseRequest;
import com.kampuskor.restservice.features.Course.DTO.CourseResponse;
import com.kampuskor.restservice.features.Course.DTO.EnrollCourseResponse;
import com.kampuskor.restservice.features.User.User;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/courses")
class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    private ResponseEntity<PageResponse<CourseResponse>> findAll(Pageable pageable) {
        PageResponse<CourseResponse> courses = courseService.findAll(pageable);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable Long courseId) {
        CourseResponse response = courseService.getCourseById(courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    private ResponseEntity<PageResponse<CourseResponse>> findAllForCurrentInstructor(
        Pageable pageable, 
        Authentication authentication
    ) {
        PageResponse<CourseResponse> courses = courseService.getCoursesByInstructor(pageable, authentication.getName());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/me/{courseId}/students")
    public ResponseEntity<PageResponse<User>> getCourseStudents(
        @PathVariable Long courseId,
        Pageable pageable, 
        Authentication authentication
    ) {
        PageResponse<User> response = courseService.getCourseStudents(courseId, pageable, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/enrolled")
    public ResponseEntity<PageResponse<CourseResponse>> getEnrolledCourses(
        Pageable pageable,
        Authentication authentication
    ) {
        PageResponse<CourseResponse> response = courseService.getEnrolledCourses(pageable, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping
    private ResponseEntity<CourseResponse> createCourse(
        @Valid @RequestBody CourseRequest course,
        Authentication authentication,
        UriComponentsBuilder uriBuilder
    ) {
        CourseResponse createdCourse = courseService.createCourse(course, authentication.getName());
        return ResponseEntity
            .created(
                uriBuilder
                    .path("/courses/{id}")
                    .buildAndExpand(createdCourse.id())
                    .toUri()
            )
            .body(createdCourse);
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollCourseResponse> enrollCourse(
        @PathVariable Long courseId, 
        Authentication authentication
    ) {
        String studentUsername = authentication.getName();
        EnrollCourseResponse response = courseService.enrollCourse(courseId, studentUsername);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}/unenroll")
    public ResponseEntity<Void> unenrollCourse(
        @PathVariable Long courseId, 
        Authentication authentication
    ) {
        String studentUsername = authentication.getName();
        courseService.unenrollCourse(courseId, studentUsername);
        return ResponseEntity.noContent().build();
    }
    
    
    @PutMapping("/{courseId}")
    private ResponseEntity<Void> updateCourse(
        @PathVariable Long courseId,
        @Valid @RequestBody CourseRequest courseRequest,
        Authentication authentication
    ) {
        courseService.updateCourse(courseId, courseRequest, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{courseId}")
    private ResponseEntity<Void> deleteCourse(
        @PathVariable Long courseId,
        Authentication authentication
    ) {
        courseService.deleteCourse(courseId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

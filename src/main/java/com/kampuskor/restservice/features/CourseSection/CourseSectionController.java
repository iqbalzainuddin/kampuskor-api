package com.kampuskor.restservice.features.CourseSection;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kampuskor.restservice.common.response.PageResponse;
import com.kampuskor.restservice.features.CourseSection.DTO.CourseSectionRequest;
import com.kampuskor.restservice.utils.security.AuthUtils;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/courses/{courseId}/sections")
class CourseSectionController {
    private final AuthUtils authUtils;
    private final CourseSectionService courseSectionService;

    public CourseSectionController(AuthUtils authUtils, CourseSectionService courseSectionService) {
        this.authUtils = authUtils;
        this.courseSectionService = courseSectionService;
    }

    @PostMapping
    private ResponseEntity<CourseSection> createCourseSection(
            @Valid @RequestBody CourseSectionRequest newCourseSection,
            @PathVariable Long courseId,
            Authentication authentication,
            UriComponentsBuilder uriBuilder
    ) {
        CourseSection createdSection = courseSectionService.createCourseSection(newCourseSection, courseId, authentication.getName());

        URI location = uriBuilder.path("/courses/{courseId}/sections/{id}")
                .buildAndExpand(courseId, createdSection.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdSection);
    }

    @GetMapping
    private ResponseEntity<PageResponse<CourseSection>> getCourseSections(
        @PathVariable Long courseId,
        Pageable pageable, 
        Authentication authentication
    ) {
        Long authenticatedUserId = authUtils.getAuthenticatedUserId();
        String authenticatedRole = authentication.getAuthorities().iterator().next().toString();
        
        Page<CourseSection> courseSections = courseSectionService.getCourseSectionsByCourse(
            courseId,
            authenticatedUserId,
            authenticatedRole,
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
        );

        return ResponseEntity.ok(
            new PageResponse<>(
                courseSections.getNumber(),
                courseSections.getNumberOfElements(),
                courseSections.getTotalPages(),
                courseSections.getTotalElements(),
                courseSections.stream().toList()
            )
        );
    }

    @GetMapping("/{sectionId}")
    private ResponseEntity<CourseSection> getCourseSection(
        @PathVariable Long courseId,
        @PathVariable Long sectionId
    ) {
        CourseSection courseSection = courseSectionService.getCourseSectionByIdAndCourseAndInstructor(
            sectionId,
            courseId,
            authUtils.getAuthenticatedUserId()    
        );

        return ResponseEntity.ok(courseSection);
    }

    @PutMapping("/{sectionId}")
    private ResponseEntity<Void> updateCourseSection(
        @PathVariable Long courseId, 
        @PathVariable Long sectionId, 
        @Valid @RequestBody CourseSectionRequest courseSection
    ) {
        Long authenticatedUserId = authUtils.getAuthenticatedUserId();

        courseSectionService.updateCourseSection(
            courseId,
            sectionId,
            courseSection,
            authenticatedUserId
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sectionId}")
    private ResponseEntity<Void> deleteCourseSection(
        @PathVariable Long courseId,
        @PathVariable Long sectionId
    ) {
        Long authenticatedUserId = authUtils.getAuthenticatedUserId();

        courseSectionService.deleteCourseSection(
            courseId,
            sectionId,
            authenticatedUserId
        );

        return ResponseEntity.noContent().build();
    }
}
